package com.github.hgaol.uiharu.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author: gaohan
 * @date: 2018-08-24 15:53
 **/
public class AspectProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    /**
     * ProxyChain的递归逻辑实际放在这里面了，如果通过递归理解会简单些
     */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;

        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        try {
            before(cls, method, params);
            result = proxyChain.doProxyChain();
            after(cls, method, params, result);
        } catch (Exception e) {
            logger.error("proxy failure", e);
            error(cls, method, params, e);
        } finally {
            end();
        }

        return result;
    }

    public void error(Class<?> cls, Method method, Object[] params, Exception e) {
    }

    public void before(Class<?> cls, Method method, Object[] params) {
    }

    public void after(Class<?> cls, Method method, Object[] params, Object result) {
    }

    public void end() {
    }

}
