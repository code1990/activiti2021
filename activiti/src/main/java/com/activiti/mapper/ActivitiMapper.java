package com.activiti.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Mapper
@Component
public interface ActivitiMapper {

    //读取表单
    @Select("SELECT Control_ID_,Control_VALUE_ from formdata where PROC_INST_ID_ = #{PROC_INST_ID}")
    List<HashMap<String, Object>> selectFormData(@Param("PROC_INST_ID") String PROC_INST_ID);

    //写入表单
    @Insert("<script> insert into formdata (PROC_DEF_ID_,PROC_INST_ID_,FORM_KEY_,Control_ID_,Control_VALUE_)" +
            "    values" +
            "    <foreach collection=\"maps\" item=\"formData\" index=\"index\" separator=\",\">" +
            "      (#{formData.PROC_DEF_ID_,jdbcType=VARCHAR},#{formData.PROC_INST_ID_,jdbcType=VARCHAR}," +
            "      #{formData.FORM_KEY_,jdbcType=VARCHAR}, #{formData.Control_ID_,jdbcType=VARCHAR},#{formData.Control_VALUE_,jdbcType=VARCHAR})" +
            "    </foreach>  </script>")
    int insertFormData(@Param("maps") List<HashMap<String, Object>> maps);


    //删除表单
    @Delete("delete from formdata where PROC_DEF_ID_ = #{PROC_DEF_ID}")
    int deleteFormData(@Param("PROC_DEF_ID") String PROC_DEF_ID);

    //获取用户名
    @Select("select name,username from user")
    List<HashMap<String,Object>> selectUser();



}
