package com.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test03ProcessInstance {

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 初始化一个流程定义
     */
    @Test
    public void getInfo111(){
        //1.获取页面表单填写的内容，比如请假信息
        //2.页面内容写入业务表，成功以后返回主键id
        //3.把业务数据与activiti7流程数据关联
        String bpmnId ="myProcess_1";//bpmn文件定义的id
        String bussinessKey ="";//自定义的业务id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnId,bussinessKey);
        //返回流程实体的流程定义id
        //流程定义与流程实例是一对多的关系
        //一个请假业务流程可能有多个人使用，去请假 产生多个流程实例
        System.out.println(processInstance.getProcessDefinitionId());
    }


    /**
     * 获取流程实例信息
     */
    @Test
    public void getInfo222(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        for (int i = 0; i < list.size(); i++) {
            ProcessInstance p = list.get(i);
            System.out.println(p.getProcessDefinitionId());
            System.out.println(p.getId());//710a6456-4bcc-11eb-b25e-6c71d96cc83a
            System.out.println(p.getProcessInstanceId());//710a6456-4bcc-11eb-b25e-6c71d96cc83a
            System.out.println(p.getName());
            System.out.println(p.getDeploymentId());
            System.out.println(p.getDescription());
            System.out.println(p.isSuspended());
            System.out.println(p.isEnded());
        }
    }

    /**
     * 流程实例终止
     * 流程实例激活
     */
    @Test
    public void getInfo333(){
        //实例id
        String instanceId = "710a6456-4bcc-11eb-b25e-6c71d96cc83a";
        //暂停
        runtimeService.suspendProcessInstanceById(instanceId);
        //激活
        runtimeService.activateProcessInstanceById(instanceId);
    }

    /**
     * 删除一个流程实例
     */
    @Test
    public void getInfo444(){
        String instanceId = "710a6456-4bcc-11eb-b25e-6c71d96cc83a";
        String info ="";//自定义删除时候的提示信息
        //删除
        runtimeService.deleteProcessInstance(instanceId,info);
    }
}
