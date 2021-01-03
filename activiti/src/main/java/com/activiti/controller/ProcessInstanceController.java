package com.activiti.controller;

import com.activiti.SecurityUtil;
import com.activiti.pojo.UserInfoBean;
import com.activiti.util.AjaxResponse;
import com.activiti.util.GlobalConfig;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.plugin.liveconnect.SecurityContextHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RepositoryService repositoryService;

    //查询所有的流程实例
    @GetMapping(value = "/getInstances")
    public AjaxResponse getInstances(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            Page<ProcessInstance> page = processRuntime.processInstances(Pageable.of(0, 50));
            List<ProcessInstance> list = page.getContent();
            list.sort((x, y) -> x.getStartDate().toString().compareTo(y.getStartDate().toString()));

            List<HashMap<String, Object>> listMap = new ArrayList<>();
            for (ProcessInstance pi : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", pi.getId());
                hashMap.put("name", pi.getName());
                hashMap.put("status", pi.getStatus());
                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
                hashMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
                hashMap.put("startDate", pi.getStartDate());
                hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());
                //需要单独查询流程部署id+资源名称
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentId", pd.getDeploymentId());
                listMap.add(hashMap);
            }
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, listMap);
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    //启动流程实例
    @GetMapping(value = "/startProcess")
    public AjaxResponse startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                     @RequestParam("instanceName") String instanceName,
                                     @RequestParam("instanceVariable") String instanceVariable) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            } else {
                securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
            }
            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start().withProcessDefinitionKey(processDefinitionKey)
                    .withName(instanceName)
                    .withBusinessKey("自定义的BusinessKey").build());
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, processInstance.getName() + ":" + processInstance.getId());
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }

    }

    //删除流程实例
    @GetMapping(value = "/deleteInstance")
    public AjaxResponse deleteInstance(@RequestParam("instanceID") String instanceID) {
        try {
            ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder.delete()
                    .withProcessInstanceId(instanceID).build());
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, processInstance.getName() + ":" + processInstance.getId());
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    //挂起流程实例
    @GetMapping(value = "suspendInstance")
    public AjaxResponse suspendInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder.suspend()
                    .withProcessInstanceId(instanceID).build());
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, processInstance.getName() + ":" + processInstance.getId());
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    //激活流程实例
    @GetMapping(value = "resumeInstance")
    public AjaxResponse resumeInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder.resume().withProcessInstanceId(instanceID).build());
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, processInstance.getName() + ":" + processInstance.getId());
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    //获取流程参数
    @GetMapping(value = "variables")
    public AjaxResponse variables(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.Test) {
                securityUtil.logInAs("bajie");
            }
            List<VariableInstance> variableInstances = processRuntime.variables(ProcessPayloadBuilder.variables().withProcessInstanceId(instanceID).build());

            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, variableInstances);
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }
}
