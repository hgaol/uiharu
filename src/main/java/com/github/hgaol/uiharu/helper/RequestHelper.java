package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.bean.Param;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author: gaohan
 * @date: 2018-08-23 17:48
 **/
public class RequestHelper {

    private static final Logger log = LoggerFactory.getLogger(RequestHelper.class);

    /**
     * 解析request中的params
     */
    public static Param createRequestParams(HttpServletRequest req) {
        Map<String, List<String>> params = new HashMap<>();

        parseRequestParams(req, params);
        parseFormParams(req, params);
        return new Param(params);
    }

    /**
     * 解析request body中的json为string，之后根据@RequestBody注解，查看是否转换为对应的body
     */
    public static String createRequestBody(HttpServletRequest req) {
        // todo: 如果是表单提交或者文件上传，则会null
        if ("application/x-www-form-urlencoden".equals(req.getContentType())) {
            return null;
        }
        try {
            String body;
            body = IOUtils.toString(req.getInputStream(), "UTF-8");
            if (!StringUtils.isEmpty(body)) {
                return body;
            }
        } catch (IOException e) {
            log.error("todo");
            throw new Error(e);
        }
        return null;
    }

    /**
     * 解析查询参数到params中
     */
    private static void parseRequestParams(HttpServletRequest req,
                                           Map<String, List<String>> params) {
        Map<String, String[]> reqParams = req.getParameterMap();
        for (Map.Entry<String, String[]> entry : reqParams.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            params.put(key, Arrays.asList(values));
        }
    }

    /**
     * 将body中的表单信息解析到params中
     */
    private static void parseFormParams(HttpServletRequest req, Map<String, List<String>> params) {
        if (!"application/x-www-form-urlencoded".equals(req.getContentType())) {
            return;
        }
        String body;
        try {
            body = IOUtils.toString(req.getInputStream(), "UTF-8");
            if (StringUtils.isEmpty(body)) {
                return;
            }
            String[] kvs = StringUtils.split(body, "&");
            if (ArrayUtils.isEmpty(kvs)) {
                return;
            }
            for (String kv : kvs) {
                String[] array = StringUtils.split(kv, "=");
                if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                    String fieldName = array[0];
                    String fieldValue = array[1];
                    // 不存在则创建，存在则添加
                    if (params.containsKey(fieldName)) {
                        params.get(fieldName).add(fieldValue);
                    } else {
                        List<String> value = new ArrayList<>();
                        value.add(fieldValue);
                        params.put(fieldName, value);
                    }
                }
            }
        } catch (IOException e) {
            log.error("parse form params failure.");
            throw new Error(e);
        }
    }

}
