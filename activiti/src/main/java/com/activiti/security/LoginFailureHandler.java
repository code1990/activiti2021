package com.activiti.security;

import com.activiti.util.AjaxResponse;
import com.activiti.util.GlobalConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("loginFailureHandler")
public class LoginFailureHandler implements AuthenticationFailureHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    //使用objectmapper把对象转为Json字符串
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.info("error");
        int status = GlobalConfig.ResponseCode.ERROR.getCode();
        String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
        String info = e.getMessage();
        AjaxResponse data = AjaxResponse.AjaxData(status, desc, info);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(data));
    }
}
