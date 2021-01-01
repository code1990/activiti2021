package com.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test04Task {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    /**
     * 先创建一张流程图
     * 然后创建一个具体的流程审批实例
     * 才能执行具体的审批流程
     * 每一个流程节点相当于一个task
     */
    @Test
    public void init() {
        String fileName = "BPMN/test04.bpmn";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(fileName)
                .name("流程审批V1")
                .deploy();
        System.out.println(deployment.getName());
        String bpmnId = "myProcess_task";//bpmn文件定义的id
        String bussinessKey = "";//自定义的业务id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnId, bussinessKey);
        //返回流程实体的流程定义id
        //myProcess_task:1:761a75f4-4c12-11eb-a38b-6c71d96cc83a
        System.out.println(processInstance.getProcessDefinitionId());
    }


    /**
     * 查询任务实例
     */
    @Test
    public void test111() {
        List<Task> tasks = taskService.createTaskQuery().list();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(task.getDelegationState());
            System.out.println(task.getId());//7153543a-4bcc-11eb-b25e-6c71d96cc83a
            System.out.println(task.getCreateTime());
            System.out.println(task.getDescription());
        }
    }

    /**
     * 查询某人当前的待办任务
     */
    @Test
    public void test222() {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("bajie")
                .list();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println(task.getDelegationState());
            System.out.println(task.getId());//7153543a-4bcc-11eb-b25e-6c71d96cc83a
            System.out.println(task.getCreateTime());
            System.out.println(task.getDescription());
        }
    }

    /**
     * 完成某个节点的任务
     * 任务完成以后会流转到下一个人
     */
    @Test
    public void test333() {
        String taskId ="7153543a-4bcc-11eb-b25e-6c71d96cc83a";
        taskService.complete(taskId);
        System.out.println("任务完成");
    }

    @Test
    public void test400() {
        String fileName = "BPMN/test04Claim.bpmn";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(fileName)
                .name("多个候选人任务")
                .deploy();
        System.out.println(deployment.getName());
        String bpmnId = "myProcess_claim";//bpmn文件定义的id
        String bussinessKey = "";//自定义的业务id
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(bpmnId, bussinessKey);
        //返回流程实体的流程定义id
        //myProcess_claim:1:dfd1c4d5-4c16-11eb-91e5-6c71d96cc83a
        System.out.println(processInstance.getProcessDefinitionId());
    }

    /**
     * 一个任务多个候选人
     * 某个候选人拾取任务
     */
    @Test
    public void test444() {
        //通过taskService.createTaskQuery().list()获取taskid
        String taskId = "dfedb14a-4c16-11eb-91e5-6c71d96cc83a";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        System.out.println(task.getId());
        taskService.claim(taskId, "bajie");
    }

    /**
     * 任务拾取错了
     * 归还任务
     * 任务缺乏执行能力 把任务交办出去给别人办理
     */
    @Test
    public void test555() {
        String taskId = "dfedb14a-4c16-11eb-91e5-6c71d96cc83a";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.setAssignee(taskId, "null");//归还候选任务
        taskService.setAssignee(taskId, "wukong");//交办
    }


}
