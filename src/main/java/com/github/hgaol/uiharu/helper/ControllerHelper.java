package com.github.hgaol.uiharu.helper;

import com.github.hgaol.uiharu.helper.ClassHelper;
import com.github.hgaol.uiharu.annotation.Action;
import com.github.hgaol.uiharu.bean.Handler;
import com.github.hgaol.uiharu.bean.Request;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: gaohan
 * @date: 2018-08-22 16:55
 **/
public class ControllerHelper {

    private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

    static {
        init();
    }

    /**
     * 初始化action_map
     */
    private static void init() {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (controllerClassSet.isEmpty()) {
            return;
        }
        for (Class<?> controllerClass : controllerClassSet) {
            Method[] methods = controllerClass.getDeclaredMethods();
            if (ArrayUtils.isEmpty(methods)) {
                return;
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(Action.class)) {
                    Action action = method.getAnnotation(Action.class);
                    String reqMethod = action.method();
                    String reqPath = action.path();
                    Request request = new Request(reqMethod, reqPath);
                    Handler handler = new Handler(controllerClass, method);
                    if (ACTION_MAP.get(request) != null) {
                        throw new Error("conflict request mapping! " + request + " [" +
                                request + "-" + handler + " vs. " + ACTION_MAP.get(request));
                    }
                    ACTION_MAP.put(request, handler);
                }
            }
        }
    }

    /**
     * 根据method和path获取对应的handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        return ACTION_MAP.get(new Request(requestMethod, requestPath));
    }

}
