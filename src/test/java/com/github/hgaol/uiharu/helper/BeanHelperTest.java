package com.github.hgaolbb.uiharu.helper;

import com.github.hgaolbb.uiharu.annotation.Controller;
import com.github.hgaolbb.uiharu.demo.ControllerDemo;
import com.github.hgaolbb.uiharu.demo.HelloDemo;
import com.github.hgaolbb.uiharu.util.ReflectionUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeanHelperTest {

    @Test
    public void getBeanMap() {
        assertFalse(BeanHelper.getBeanMap().isEmpty());
    }

    @Test
    public void getBean() {
        assertNotNull(BeanHelper.getBean(ControllerDemo.class));
    }

    @Test
    public void setBean() {
        BeanHelper.setBean(HelloDemo.class, ReflectionUtils.newInstance(HelloDemo.class));
        assertNotNull(BeanHelper.getBean(HelloDemo.class));
    }
}