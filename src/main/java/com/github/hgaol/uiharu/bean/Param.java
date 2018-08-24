package com.github.hgaol.uiharu.bean;

import com.github.hgaol.uiharu.util.CastUtils;

import java.util.List;
import java.util.Map;

/**
 * @author: gaohan
 * @date: 2018-08-23 17:49
 **/
public class Param {

    private Map<String, List<String>> requestParams;

    public Param(Map<String, List<String>> requestParams) {
        this.requestParams = requestParams;
    }

    public boolean isEmpty() {
        return requestParams.isEmpty();
    }

    public boolean getBoolean(String name) {
        List<String> values = requestParams.get(name);
        if (values.size() == 1) {
            return CastUtils.castBoolean(values.get(0));
        } else {
            throw new Error("wrong number parameters.");
        }
    }

    public String getString(String name) {
        List<String> values = requestParams.get(name);
        if (values.size() == 1) {
            return values.get(0);
        } else {
            throw new Error("wrong number parameters.");
        }
    }

    public int getInt(String name) {
        List<String> values = requestParams.get(name);
        if (values.size() == 1) {
            return CastUtils.castInt(values.get(0));
        } else {
            throw new Error("wrong number parameters.");
        }
    }

    public long getLong(String name) {
        List<String> values = requestParams.get(name);
        if (values.size() == 1) {
            return CastUtils.castLong(values.get(0));
        } else {
            throw new Error("wrong number parameters.");
        }
    }

    public double getDouble(String name) {
        List<String> values = requestParams.get(name);
        if (values.size() == 1) {
            return CastUtils.castDouble(values.get(0));
        } else {
            throw new Error("wrong number parameters.");
        }
    }

    public List<String> getValues(String name) {
        return requestParams.get(name);
    }

    @Override
    public String toString() {
        return "Param{" +
                "requestParams=" + requestParams +
                '}';
    }
}
