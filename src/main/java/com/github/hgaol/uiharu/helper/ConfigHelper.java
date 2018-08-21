package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.util.PropUtils;
import com.github.hgaol.uiharu.constant.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author: gaohan
 * @date: 2018-08-21 14:04
 **/
public class ConfigHelper {

    private static final Logger log = LoggerFactory.getLogger(ConfigHelper.class);

    private static final Properties CONFIG_PROPS = PropUtils.loadProps(ConfigConstants.CONFIG_FILE);

    /**
     * 获取 JDBC 驱动
     */
    public static String getJdbcDriver() {
        return PropUtils.getString(CONFIG_PROPS, ConfigConstants.JDBC_DRIVER);
    }

    /**
     * 获取 JDBC URL
     */
    public static String getJdbcUrl() {
        return PropUtils.getString(CONFIG_PROPS, ConfigConstants.JDBC_URL);
    }

    /**
     * 获取 JDBC 用户名
     */
    public static String getJdbcUsername() {
        return PropUtils.getString(CONFIG_PROPS, ConfigConstants.JDBC_USERNAME);
    }

    /**
     * 获取 JDBC 密码
     */
    public static String getJdbcPassword() {
        return PropUtils.getString(CONFIG_PROPS, ConfigConstants.JDBC_PASSWORD);
    }

    /**
     * 获取应用基础包名
     */
    public static String getAppBasePackage() {
        return PropUtils.getString(CONFIG_PROPS, ConfigConstants.APP_BASE_PACKAGE);
    }

    /**
     * 获取应用 JSP 路径
     */
    public static String getAppJspPath() {
        return PropUtils.getString(CONFIG_PROPS, ConfigConstants.APP_JSP_PATH, "/WEB-INF/view/");
    }

    /**
     * 获取应用静态资源路径
     */
    public static String getAppAssetPath() {
        return PropUtils.getString(CONFIG_PROPS, ConfigConstants.APP_ASSET_PATH, "/asset/");
    }

    /**
     * 获取应用文件上传限制
     */
    public static int getAppUploadLimit() {
        return PropUtils.getInt(CONFIG_PROPS, ConfigConstants.APP_UPLOAD_LIMIT, 10);
    }

    /**
     * 根据属性名获取 String 类型的属性值
     */
    public static String getString(String key) {
        return PropUtils.getString(CONFIG_PROPS, key);
    }

    /**
     * 根据属性名获取 int 类型的属性值
     */
    public static int getInt(String key) {
        return PropUtils.getInt(CONFIG_PROPS, key);
    }

}
