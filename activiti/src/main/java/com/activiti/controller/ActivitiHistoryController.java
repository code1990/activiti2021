package com.activiti.controller;

import org.activiti.bpmn.model.*;
import com.activiti.SecurityUtil;
import com.activiti.pojo.UserInfoBean;
import com.activiti.util.AjaxResponse;
import com.activiti.util.GlobalConfig;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/activitiHistory")
public class ActivitiHistoryController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    //用户历史
    @GetMapping(value = "getInstanceByUserName")
    public AjaxResponse getInstanceByUserName() {
        try {
            List<HistoricTaskInstance> historyTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().asc()
                    .taskAssignee("bajie")
                    .list();

            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, historyTaskInstances);
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    //任务实例历史
    @GetMapping(value = "getInstanceByPiID")
    public AjaxResponse getInstanceByPiID(@RequestParam("piID") String piID) {
        try {
            List<HistoricTaskInstance> historyTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().asc()
                    .processInstanceId(piID)
                    .list();

            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, historyTaskInstances);
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    //流程图历史高亮
    @GetMapping(value = "/getHighLine")
    public AjaxResponse getHighLine(@RequestParam("instanceId") String instanceId,
                                    @AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceId).singleResult();
            //获取bpmnModel对象
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            Process process = bpmnModel.getProcesses().get(0);

            //获取所有的flowelement信息
            Collection<FlowElement> flowElements = process.getFlowElements();
            Map<String, String> map = new HashMap<>();
            for (FlowElement flowElement : flowElements) {
                //判断是否是连线
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sq = (SequenceFlow) flowElement;
                    String ref = sq.getSourceRef();
                    String targetRef = sq.getTargetRef();
                    map.put(ref + targetRef, sq.getId());
                }
            }
            //获取流程实例 历史节点
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .list();
            //各个历史节点 两两组合
            Set<String> keyList = new HashSet<>();
            for (HistoricActivityInstance i : list) {
                for (HistoricActivityInstance j : list) {
                    if (i != j) {
                        keyList.add(i.getActivityId() + j.getActivityId());
                    }
                }
            }
            //高亮连线Id
            Set<String> highLine = new HashSet<>();
            keyList.forEach(s -> highLine.add(map.get(s)));

            //获取流程实例 历史节点已完成
            List<HistoricActivityInstance> finishedList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .finished()
                    .list();

            //高亮节点ID
            Set<String> highPoint = new HashSet<>();
            finishedList.forEach(s -> highPoint.add(s.getActivityId()));

            //获取流程实例 历史节点没有完成
            List<HistoricActivityInstance> unFinishedList = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceId)
                    .unfinished()
                    .list();

            //需要移除的高亮的连线
            Set<String> set = new HashSet<>();
            //待办高亮节点
            Set<String> waitTodo = new HashSet<>();
            unFinishedList.forEach(s -> {
                waitTodo.add(s.getActivityId());
                for (FlowElement flowElement : flowElements) {
                    if (flowElement instanceof UserTask) {
                        UserTask userTask = (UserTask) flowElement;
                        if (userTask.getId().equals(s.getActivityId())) {
                            List<SequenceFlow> outFlows = userTask.getOutgoingFlows();
                            if (outFlows != null && outFlows.size() > 0) {
                                outFlows.forEach(a -> {
                                    if (a.getSourceRef().equals(s.getActivityId())) {
                                        set.add(a.getId());
                                    }
                                });
                            }
                        }
                    }
                }
            });

            highLine.removeAll(set);

            Set<String> iDo = new HashSet<>();
            String assigneeName = null;
            if (GlobalConfig.Test) {
                assigneeName = "bajie";
            } else {
                assigneeName = userInfoBean.getUsername();
            }

            List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(assigneeName)
                    .finished()
                    .processInstanceId(instanceId).list();
            taskInstances.forEach(a -> iDo.add(a.getTaskDefinitionKey()));

            Map<String, Object> reMap = new HashMap<>();
            reMap.put("highPoint", highPoint);
            reMap.put("highLine", highLine);
            reMap.put("waitingToDo", waitTodo);
            reMap.put("iDo", iDo);

            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, reMap);
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }
}
