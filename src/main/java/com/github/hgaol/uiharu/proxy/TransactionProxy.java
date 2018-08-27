package com.github.hgaol.uiharu.proxy;

import com.github.hgaol.uiharu.annotation.Transaction;
import com.github.hgaol.uiharu.helper.DatabaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author: gaohan
 * @date: 2018-08-27 09:28
 **/
public class TransactionProxy implements Proxy {

    private static final Logger log = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    /**
     * 初始化的时候对@Service注解的类进行代理，然后在此处检查某个方法是否使用了Transaction注解
     * 如果使用了，则启用事务
    */
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = FLAG_HOLDER.get();
        Method method = proxyChain.getTargetMethod();
        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAG_HOLDER.set(true);
            try {
                DatabaseHelper.beginTransaction();
                log.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                log.debug("commit transaction");
            } catch (Throwable e) {
                DatabaseHelper.rollbackTransaction();
                log.debug("rollback transaction");
                throw e;
            } finally {
                FLAG_HOLDER.remove();
            }
        } else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }

}
