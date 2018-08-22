package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.demo.ControllerDemo;
import com.github.hgaol.uiharu.demo.HelloDemo;
import com.github.hgaol.uiharu.util.ReflectionUtils;
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