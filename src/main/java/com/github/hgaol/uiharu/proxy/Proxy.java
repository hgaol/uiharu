package com.github.hgaol.uiharu.proxy;

/**
 * @author: gaohan
 * @date: 2018-08-24 15:45
 **/
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
