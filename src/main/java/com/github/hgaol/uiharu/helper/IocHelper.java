package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.annotation.Inject;
import com.github.hgaol.uiharu.util.ReflectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 获取所有Bean，进行依赖注入
 * 因为是单例的，所以注入的最终都是"完全体"
 *
 * @author: gaohan
 * @date: 2018-08-22 14:40
 **/
public class IocHelper {

    static {
        init();
    }

    private static void init() {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (beanMap.isEmpty()) {
            return;
        }
        for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
            Class<?> beanClass = entry.getKey();
            Object beanInstance = entry.getValue();
            Field[] beanFields = beanClass.getDeclaredFields();
            // 如果没有field，则无需注入
            if (ArrayUtils.isEmpty(beanFields)) {
                continue;
            }
            for (Field field : beanFields) {
                // 如果有Inject注解，则需要注入
                if (field.isAnnotationPresent(Inject.class)) {
                    Class<?> fieldClass = field.getType();
                    Object fieldInstance = beanMap.get(fieldClass);
                    // 进行注入
                    if (fieldInstance != null) {
                        ReflectionUtils.setField(beanInstance, field, fieldInstance);
                    } else {
                        // 找不到对应的bean，抛出异常
                        throw new Error("can not find bean " + fieldClass.getSimpleName()
                                + " injected by " + beanClass.getSimpleName());
                    }
                }
            }
        }
    }

}
