package com.github.hgaol.uiharu.annotation;

import java.lang.annotation.*;

/**
 * @author: gaohan
 * @date: 2018-08-24 16:38
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
