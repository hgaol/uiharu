package com.github.hgaol.uiharu.demo;

import com.github.hgaol.uiharu.annotation.Controller;
import com.github.hgaol.uiharu.annotation.Inject;

/**
 * @author: gaohan
 * @date: 2018-08-21 19:04
 **/
@Controller
public class ControllerDemo {

    @Inject
    public ServiceDemo serviceDemo;
}
