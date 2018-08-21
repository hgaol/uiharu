package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.annotation.Service;
import com.github.hgaol.uiharu.annotation.Controller;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.junit.Assert.*;

public class ClassHelperTest {

    @Test
    public void getClassSet() {
        System.out.println("--- getClassSet --- ");
        Set<Class<?>> classSet = ClassHelper.getClassSet();
        System.out.println(classSet);
    }

    @Test
    public void getServiceClassSet() {
        System.out.println("--- getServiceClassSet --- ");
        Set<Class<?>> classSet = ClassHelper.getServiceClassSet();
        System.out.println(classSet);
        for (Class<?> cls : classSet) {
            assertEquals(true, cls.isAnnotationPresent(Service.class));
        }
    }

    @Test
    public void getControllerClassSet() {
        System.out.println("--- getControllerClassSet --- ");
        Set<Class<?>> classSet = ClassHelper.getControllerClassSet();
        System.out.println(classSet);
        for (Class<?> cls : classSet) {
            assertEquals(true, cls.isAnnotationPresent(Controller.class));
        }
    }

    @Test
    public void getBeanClassSet() {
        System.out.println("--- getBeanClassSet --- ");
        System.out.println(ClassHelper.getBeanClassSet());
    }

    @Test
    public void getClassSetBySuper() {
        System.out.println("--- getClassSetBySuper --- ");
        Set<Class<?>> classSet = ClassHelper.getClassSetBySuper(Annotation.class);
        System.out.println(classSet);
        for (Class<?> cls : classSet) {
            assertEquals(true, Annotation.class.isAssignableFrom(cls));
        }
    }
}