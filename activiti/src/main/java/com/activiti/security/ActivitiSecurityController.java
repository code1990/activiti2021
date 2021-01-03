package com.activiti.security;

import com.sun.deploy.net.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ActivitiSecurityController {

    //在springSecurity登录验证之后，对未登录用户跳转行为进行的判断
    @RequestMapping("/login")
    @ResponseStatus(code= HttpStatus.UNAUTHORIZED)
    public HttpResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response){
        return null;
    }
}
