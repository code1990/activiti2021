package com.activiti.security;

import com.activiti.mapper.UserInfoBeanMapper;
import com.activiti.pojo.UserInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserInfoBeanMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfoBean userInfoBean = mapper.selectUserByUsername(username);
        if (userInfoBean == null) {
            throw new UsernameNotFoundException("查无此人");
        }
        return userInfoBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
