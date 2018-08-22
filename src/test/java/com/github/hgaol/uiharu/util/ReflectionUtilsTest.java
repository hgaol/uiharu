package com.github.hgaolbb.uiharu.util;

import com.github.hgaolbb.uiharu.demo.HelloDemo;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ReflectionUtilsTest {

    @Test
    public void newInstance() {
        Object obj = ReflectionUtils.newInstance(ReflectionUtils.class);
        assertTrue(obj instanceof ReflectionUtils);
    }

    @Test
    public void newInstance1() {
        Object obj = ReflectionUtils.newInstance("com.github.hgaolbb.uiharu.util.ReflectionUtils");
        assertTrue(obj instanceof ReflectionUtils);
    }

    @Test
    public void invokeMethod() {
        Method[] methods = HelloDemo.class.getDeclaredMethods();
        HelloDemo helloDemo = (HelloDemo) ReflectionUtils.newInstance(HelloDemo.class);
        for (Method method : methods) {
            ReflectionUtils.invokeMethod(helloDemo, method);
        }
    }

    @Test
    public void setField() {
        try {
            Object obj = ReflectionUtils.newInstance(HelloDemo.class);
            Field field = obj.getClass().getDeclaredField("name");
            ReflectionUtils.setField(obj, field, "uiharu!");
            assertEquals("uiharu!", ((HelloDemo) obj).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}