package com.activiti.mapper;

import com.activiti.pojo.UserInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserInfoBeanMapper {

    @Select("select * from user where username = #{username}")
    UserInfoBean selectUserByUsername(@Param("username") String username);
}
