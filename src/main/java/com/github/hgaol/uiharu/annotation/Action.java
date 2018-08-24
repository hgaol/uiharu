package com.github.hgaol.uiharu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: gaohan
 * @date: 2018-08-22 16:36
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    /**
     * 请求路径
     */
    String method() default "get";

    /**
     * 请求方法
     */
    String path();
}
