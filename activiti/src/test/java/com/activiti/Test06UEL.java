package com.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class Test06UEL {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 启动流程携带动态参数
     */
    @Test
    public void test111(){
        Map<String,Object> map = new HashMap<>();
        //BPMN图携带参数 {zhixingren}
        map.put("zhixingren","bajie");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myProcess_UEL_V1","bKey002",map);
        System.out.println(processInstance.getDeploymentId());
    }

    /**
     * 结束流程携带动态参数
     */
    @Test
    public void test222(){
        Map<String,Object> map = new HashMap<>();
        //BPMN图携带参数 ${pay<=100} ${pay>100}
        map.put("pay","101");
        String taskId ="";
        taskService.complete(taskId,map);
    }

    /**
     * 启动流程 携带实体类参数
     */
    @Test
    public void test333(){
        UELPOJO uel = new UELPOJO();
        uel.setZhxingren("bajie");
        Map<String,Object> map = new HashMap<>();
        //BPMN图携带参数 ${uelpojo.zhixingren}
        map.put("uelpojo",uel);
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myProcess_UEL_V3","",map);
        System.out.println(processInstance.getDeploymentId());
    }

    /**
     * 结束流程携带多个参数指定候选人
     */
    @Test
    public void test444(){
        Map<String,Object> map = new HashMap<>();
        //BPMN图携带参数 ${houxuanren}
        map.put("houxuanren","bajie,tangseng");
        String taskId ="";
        taskService.complete(taskId,map);
    }

    /**
     * 直接指定全局变量 多个流程实例通用
     * 多次赋值 后者覆盖前者
     */
    @Test
    public void test555(){
        String instanceId ="";
        //直接指定流程中使用的单个变量
        runtimeService.setVariable(instanceId,"pay","101");
        //直接指定流程中使用的多个变量
        Map<String,Object> map = new HashMap<>();
        //BPMN图携带参数 ${houxuanren}
        map.put("houxuanren","bajie,tangseng");
        runtimeService.setVariables(instanceId,map);
//        taskService.setVariable();
//        taskService.setVariables();

    }

    /**
     * 直接指定局部变量 某些特定节点特定流程使用
     * 多次赋值 后者覆盖前者
     */
    @Test
    public void test666(){
        String instanceId ="";
        runtimeService.setVariableLocal(instanceId,"pay","101");
        //直接指定流程中使用的多个变量
        Map<String,Object> map = new HashMap<>();
        //BPMN图携带参数 ${houxuanren}
        map.put("houxuanren","bajie,tangseng");
        runtimeService.setVariablesLocal(instanceId,map);
    }
}

class UELPOJO{
    private String zhxingren;
    private String pay;

    public String getZhxingren() {
        return zhxingren;
    }

    public void setZhxingren(String zhxingren) {
        this.zhxingren = zhxingren;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}

