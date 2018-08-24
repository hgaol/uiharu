package com.github.hgaol.uiharu.util;

import com.github.hgaol.uiharu.helper.ConfigHelper;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClassUtilsTest {

    @Test
    public void getClassLoader() {
        ClassLoader loader = ClassUtils.getClassLoader();
        assertNotNull(ClassUtils.getClassLoader());
    }

    @Test
    public void loadClass() {
        ClassUtils.loadClass("com.github.hgaol.uiharu.helper.ClassHelper");
    }

    @Test
    public void loadClass1() {
        Class<?> cls = ClassUtils.loadClass("com.github.hgaol.uiharu.helper.ClassHelper",
                true);
        System.out.println(cls);
    }

    @Test
    public void getClassSet() {
        assertNotEquals(0, ClassUtils.getClassSet(ConfigHelper.getAppBasePackage()));
    }

}