package com.github.hgaol.uiharu.bean;

/**
 * @author: gaohan
 * @date: 2018-08-23 18:54
 **/
public class RequestParam {
    private String name;
    private String[] values;

    public RequestParam(String name, String[] values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public String[] getValues() {
        return values;
    }
}
