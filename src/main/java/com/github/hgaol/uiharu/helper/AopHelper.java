package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.annotation.Aspect;
import com.github.hgaol.uiharu.proxy.AspectProxy;
import com.github.hgaol.uiharu.proxy.Proxy;
import com.github.hgaol.uiharu.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 根据切面类和目标类生成代理类，并放入BeanMap中
 *
 * @author: gaohan
 * @date: 2018-08-24 16:11
 **/
public class AopHelper {

    private static final Logger log = LoggerFactory.getLogger(AopHelper.class);

    static {
        init();
    }

    private static void init() {
        try {

            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                // 将目标类替换为proxy实例
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            log.error("create aop failure.");
            throw new Error(e);
        }
    }

    /**
     * 扫描切面类，生成{@code Map<aspect, Set<target>>}的集合，用来之后生成切面链
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        addAspectProxy(proxyMap);
        return proxyMap;
    }

    /**
     * 将继承了AspectProxy类并且带有注解Aspect的类放入proxyMap中
     * @param proxyMap 结果集合
     * @throws Exception
     */
    private static void addAspectProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        // 获取所有继承了AspectProxy的类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        // 遍历所有实现类，检查是否使用注解Aspect，并获取Aspect.value，得到使用了value注解的Set集合
        // 该Set集合就是对应的集成了AspectProxy的代理类需要代理的目标
        for (Class<?> proxyClass : proxyClassSet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                // proxyClass 切面类
                // targetClassSet 该切面类代理的类的集合
                // 比如@Aspect(Controller.class)，targetClassSet就是所有使用了Controller.class注解的类
                proxyMap.put(proxyClass, targetClassSet);
            }
        }
    }

    /**
     * 根据aspect中value的注解类，获取所有使用了该注解的类
     * @param aspect
     * @return
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) {
        Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if (!annotation.equals(Aspect.class)) {
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取目标类的切面类List
     * key是被目标类
     * value是代理该类的切面类的list
     *
     * @param proxyMap
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            // 获取代理类
            Class<?> proxyClass = proxyEntry.getKey();
            // 获取目标对象（需要被代理类代理的对象）
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                // 因为代理类是AspectProxy的子类，所以Proxy是代理类的爷爷
                Proxy proxy = (Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }

}
