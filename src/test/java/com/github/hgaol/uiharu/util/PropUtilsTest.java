package com.github.hgaol.uiharu.util;

import com.github.hgaol.uiharu.constant.ConfigConstants;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class PropUtilsTest {

    private Properties prop = PropUtils.loadProps(ConfigConstants.CONFIG_FILE);

    @Test
    public void getString() {
        String jdbcDriver = PropUtils.getString(prop, ConfigConstants.JDBC_DRIVER);
        assertEquals("com.mysql.jdbc.Driver", jdbcDriver);
    }

    @Test
    public void getString1() {
        String s1 = PropUtils.getString(prop, ConfigConstants.APP_JSP_PATH, "hello");
        String s2 = PropUtils.getString(prop, ConfigConstants.JDBC_USERNAME, "hello");

        assertEquals("hello", s1);
        assertNotEquals("hello", s2);
    }

    @Test
    public void getInt() {
        int i1 = PropUtils.getInt(prop, "test.int");

        assertEquals(1, i1);
    }

    @Test
    public void getInt1() {
        int i1 = PropUtils.getInt(prop, "demo.null", 1);
        assertEquals(1, i1);
    }

    @Test
    public void getBoolean() {
        boolean t = PropUtils.getBoolean(prop, "test.true");
        boolean f = PropUtils.getBoolean(prop, "test.false");

        assertEquals(true, t);
        assertEquals(false, f);
    }

    @Test
    public void getBoolean1() {
        boolean t = PropUtils.getBoolean(prop, "test.true", false);
        boolean f = PropUtils.getBoolean(prop, "demo.fals", false);

        assertEquals(true, t);
        assertEquals(false, f);
    }
}