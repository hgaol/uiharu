package com.github.hgaol.uiharu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: gaohan
 * @date: 2018-08-21 19:37
 **/
public class ReflectionUtils {

    private static final Logger log = LoggerFactory.getLogger(PropUtils.class);

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls) {
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            log.error("new instance failure", e);
            throw new Error(e);
        }
        return instance;
    }

    /**
     * 创建实例（根据类名）
     */
    public static Object newInstance(String className) {
        Class<?> cls = ClassUtils.loadClass(className);
        return newInstance(cls);
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (Exception e) {
            log.error("invoke method failure", e);
            throw new Error(e);
        }
    }

    /**
     * 设置成员变量
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            log.error("set field failure", e);
            throw new Error(e);
        }
    }

}
