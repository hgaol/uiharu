package com.github.hgaol.uiharu.helper;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author: gaohan
 * @date: 2018-08-24 17:34
 **/
public final class DatabaseHelper {

    private static final Logger log = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    private static final BasicDataSource DATA_SOURCE;

    private static final String TABLE_SEPERATOR = "_";

    private static final String COLOUM_SEPERATOR = "_";

    private static final String GET_METHOD_PREFIX_GET = "get";

    private static final String GET_METHOD_PREFIX_IS = "is";

    static {
        CONNECTION_HOLDER = new ThreadLocal<>();

        QUERY_RUNNER = new QueryRunner();

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(ConfigHelper.getJdbcDriver());
        DATA_SOURCE.setUrl(ConfigHelper.getJdbcUrl());
        DATA_SOURCE.setUsername(ConfigHelper.getJdbcUsername());
        DATA_SOURCE.setPassword(ConfigHelper.getJdbcPassword());
    }

    /**
     * 获取数据源
     */
    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                log.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 开启事务
     */
    public static void beginTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                log.error("begin transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                log.error("commit transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                log.error("rollback transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            log.error("query entity failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 查询实体列表
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            log.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询并返回单个列值
     */
    public static <T> T query(String sql, Object... params) {
        T obj;
        try {
            Connection conn = getConnection();
            obj = QUERY_RUNNER.query(conn, sql, new ScalarHandler<T>(), params);
        } catch (SQLException e) {
            log.error("query failure", e);
            throw new RuntimeException(e);
        }
        return obj;
    }

    /**
     * 查询并返回多个列值
     */
    public static <T> List<T> queryList(String sql, Object... params) {
        List<T> list;
        try {
            Connection conn = getConnection();
            list = QUERY_RUNNER.query(conn, sql, new ColumnListHandler<T>(), params);
        } catch (SQLException e) {
            log.error("query list failure", e);
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * 查询并返回多个列值（具有唯一性）
     */
    public static <T> Set<T> querySet(String sql, Object... params) {
        Collection<T> valueList = queryList(sql, params);
        return new LinkedHashSet<T>(valueList);
    }

    /**
     * 查询并返回数组
     */
    public static Object[] queryArray(String sql, Object... params) {
        Object[] resultArray;
        try {
            Connection conn = getConnection();
            resultArray = QUERY_RUNNER.query(conn, sql, new ArrayHandler(), params);
        } catch (SQLException e) {
            log.error("query array failure", e);
            throw new RuntimeException(e);
        }
        return resultArray;
    }

    /**
     * 查询并返回数组列表
     */
    public static List<Object[]> queryArrayList(String sql, Object... params) {
        List<Object[]> resultArrayList;
        try {
            Connection conn = getConnection();
            resultArrayList = QUERY_RUNNER.query(conn, sql, new ArrayListHandler(), params);
        } catch (SQLException e) {
            log.error("query array list failure", e);
            throw new RuntimeException(e);
        }
        return resultArrayList;
    }

    /**
     * 查询并返回结果集映射（列名 => 列值）
     */
    public static Map<String, Object> queryMap(String sql, Object... params) {
        Map<String, Object> resultMap;
        try {
            Connection conn = getConnection();
            resultMap = QUERY_RUNNER.query(conn, sql, new MapHandler(), params);
        } catch (SQLException e) {
            log.error("query map failure", e);
            throw new RuntimeException(e);
        }
        return resultMap;
    }

    /**
     * 查询并返回结果集映射列表（列名 => 列值）
     */
    public static List<Map<String, Object>> queryMapList(String sql, Object... params) {
        List<Map<String, Object>> resultMapList;
        try {
            Connection conn = getConnection();
            resultMapList = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            log.error("query map list failure", e);
            throw new RuntimeException(e);
        }
        return resultMapList;
    }

    /**
     * 执行更新语句（包括：update、insert、delete）
     */
    public static int update(String sql, Object... params) {
        int rows;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            log.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * todo 根据id判断，如果存在则update，不存在则insert
     */
    public static boolean save(Object entity) {
        return false;
    }

    /**
     * 插入新的数据
     *
     * @param entity 实体
     * @return 是否成功
     */
    public static boolean insert(Object entity) {
        if (entity == null) {
            log.error("entity is empty.");
            return false;
        }

        String tableName = capitalizeToPosix(entity.getClass().getSimpleName(), TABLE_SEPERATOR);
        Map<String, Object> columnMap = getColoums(entity);
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " ");
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : columnMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql.append(columns).append(" VALUES ").append(values);

        Object[] params = columnMap.values().toArray();

        return update(sql.toString(), params) == 1;
    }

    /**
     * 根据id更新
     */
    public static boolean update(Object entity) {
        if (entity == null) {
            log.error("entity is empty.");
            return false;
        }

        String tableName = capitalizeToPosix(entity.getClass().getSimpleName(), TABLE_SEPERATOR);
        Map<String, Object> columnMap = getColoums(entity);
        if (!columnMap.containsKey("id") || columnMap.get("id") == null) {
            return false;
        }
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (Map.Entry<String, Object> entry : columnMap.entrySet()) {
            if ("id".equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            sql.append(entry.getKey()).append("= ?,");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" WHERE id = ?");
        Object id = columnMap.get("id");
        columnMap.remove("id");
        List<Object> paramList = new ArrayList<>(columnMap.values());
        paramList.add(id);

        Object[] params = paramList.toArray();
        System.out.println(sql.toString());
        System.out.println(Arrays.toString(params));

        return update(sql.toString(), params) == 1;
    }

    public static Map<String, Object> getColoums(Object entity) {
        // 获取fields的name，获取value
        Map<String, Object> ret = new HashMap<>();
        Field[] fields = entity.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return ret;
        }
        for (Field field : fields) {
            String coloumName = capitalizeToPosix(field.getName(), COLOUM_SEPERATOR);
            Object coloumValue = getColoumValue(field, entity);
            ret.put(coloumName, coloumValue);
        }
        return ret;
    }

    public static Object getColoumValue(Field field, Object entity) {
        Method method;
        try {
            method = getGetterMethod(field, GET_METHOD_PREFIX_GET);
            if (method == null) {
                method = getGetterMethod(field, GET_METHOD_PREFIX_IS);
            }
            if (method == null) {
                throw new Error("can not find getter method of field " + field.getName());
            }
            method.setAccessible(true);
            return method.invoke(entity);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("can not invoke getter method of field: " + field.getName());
            throw new Error(e);
        }
    }

    public static Method getGetterMethod(Field field, String prefix) {
        try {
            return field.getDeclaringClass().getDeclaredMethod(prefix + StringUtils.capitalize(field.getName()));
        } catch (NoSuchMethodException e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public static String capitalizeToPosix(String cap, String seperator) {
        if (cap == null || StringUtils.isEmpty(cap)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cap.length(); i++) {
            char c = cap.charAt(i);
            if (i == 0) {
                sb.append(Character.toLowerCase(c));
            } else {
                if (Character.isUpperCase(c)) {
                    sb.append(seperator).append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }

        }
        return sb.toString();
    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (MapUtils.isEmpty(fieldMap)) {
            log.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + entityClass.getSimpleName() + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return update(sql, params) == 1;
    }

    /**
     * 删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + entityClass.getSimpleName() + " WHERE id = ?";
        return update(sql, id) == 1;
    }

}
