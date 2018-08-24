package com.github.hgaol.uiharu.bean;

import java.lang.reflect.Method;

/**
 * @author: gaohan
 * @date: 2018-08-22 16:44
 **/
public class Handler {

    private Class<?> controllerClass;

    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    @Override
    public String toString() {
        return controllerClass + ":" + actionMethod;
    }
}
