package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.demo.ControllerDemo;
import com.github.hgaol.uiharu.util.ClassUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IocHelperTest {

    @Before
    public void before() {
        ClassUtils.loadClass("com.github.hgaol.uiharu.helper.IocHelper");
    }

    @Test
    public void test() {
        ControllerDemo demo = BeanHelper.getBean(ControllerDemo.class);
        assertNotNull(demo.serviceDemo);
        demo.serviceDemo.hi();
    }

}