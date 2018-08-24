package com.github.hgaol.uiharu.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author: gaohan
 * @date: 2018-08-24 16:06
 **/
public class ProxyManager {

    /**
     * 使用cglib生成代理类实例，目标类为targetClass
     * @param targetClass 目标类
     * @param proxyList 调用链
     * @param <T> 返回的代理类类型
     * @return 代理类实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass, o, method, methodProxy, params, proxyList).doProxyChain();
            }
        });
    }
}
