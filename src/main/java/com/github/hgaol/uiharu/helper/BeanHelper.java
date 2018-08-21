package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 获取Bean映射，获取Bean实例，设置Bean实例
 *
 * @author: gaohan
 * @date: 2018-08-21 19:36
 **/
public class BeanHelper {

    private static final Logger log = LoggerFactory.getLogger(ConfigHelper.class);

    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    /**
     * 初始化Bean映射
     */
    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> cls : beanClassSet) {
            BEAN_MAP.put(cls, ReflectionUtils.newInstance(cls));
        }
    }

    /**
     * 获取Bean映射
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取Bean实例
     */
    public static <T> T getBean(Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new Error("can not find bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 设置Bean实例
     */
    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }

}
