package com.github.hgaol.uiharu.annotation;

import java.lang.annotation.*;

/**
 * @author: gaohan
 * @date: 2018-08-24 09:58
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Body {
}
