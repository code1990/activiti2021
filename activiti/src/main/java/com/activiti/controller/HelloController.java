package com.activiti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
//controller必须在DemoApplication的子目录下面 否则找不到
@RestController
public class HelloController {

    @RequestMapping(value = "hello",method = RequestMethod.GET)
    public String hello(){
        return "hello activiti";
    }
}
