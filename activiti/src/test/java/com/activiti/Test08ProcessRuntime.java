package com.activiti;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
public class Test08ProcessRuntime {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 使用新版api获取流程实例
     */
    @Test
    public void getInfo111() {
        securityUtil.logInAs("bajie");
        //获取前100个分页流程实例
        Page<ProcessInstance> processInstancePage =
                processRuntime.processInstances(Pageable.of(0, 100));
        System.out.println(processInstancePage.getTotalItems());
        List<ProcessInstance> list = processInstancePage.getContent();
        for (ProcessInstance p : list) {
            System.out.println(p.getId());
        }

    }

    /**
     * 启动流程实例
     */
    @Test
    public void getInfo222() {
        securityUtil.logInAs("bajie");
        processRuntime.start(ProcessPayloadBuilder.start()
                .withProcessDefinitionKey("myProcess_runtime")
                .withName("第一个流程实例")
                .withBusinessKey("自定义业务key")
                .build());
    }

    /**
     * 删除流程实例
     */
    @Test
    public void getInfo333() {
        securityUtil.logInAs("bajie");
        processRuntime.delete(ProcessPayloadBuilder
                .delete()
                .withProcessInstanceId("instanceId").build());
    }

    /**
     * 挂起流程实例
     */
    @Test
    public void getInfo444() {
        securityUtil.logInAs("bajie");
        processRuntime.suspend(ProcessPayloadBuilder.suspend()
                .withProcessInstanceId("instanceId")
                .build());
    }

    /**
     * 激活流程实例
     */
    @Test
    public void getInfo555() {
        securityUtil.logInAs("bajie");
        processRuntime.resume(ProcessPayloadBuilder.resume()
                .withProcessInstanceId("instanceId")
                .build());
    }

    /**
     * 流程实例参数
     */
    @Test
    public void getVariable() {
        securityUtil.logInAs("bajie");
        List<VariableInstance> list = processRuntime.variables(ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId("instanceId").build());
        for (VariableInstance vi : list) {
            System.out.println(vi.getName());
        }
    }
}
