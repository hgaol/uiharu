package com.github.hgaol.uiharu;

import com.github.hgaol.uiharu.helper.BeanHelper;
import com.github.hgaol.uiharu.helper.ClassHelper;
import com.github.hgaol.uiharu.helper.IocHelper;
import com.github.hgaol.uiharu.util.ClassUtils;

/**
 * 加载类
 *
 * @author: gaohan
 * @date: 2018-08-22 15:07
 **/
public class HelperLoader {

    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtils.loadClass(cls.getName());
        }
    }

}
