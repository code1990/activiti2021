package com.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test05HistoricTaskInstance {

    @Autowired
    private HistoryService historyService;

    /**
     * 根据用户查询历史任务信息
     */
    @Test
    public void get111(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .taskAssignee("bajie")
                .list();
        for (int i = 0; i <list.size() ; i++) {
            HistoricTaskInstance h = list.get(i);
            System.out.println(h.getClaimTime());
            System.out.println(h.getId());
            System.out.println(h.getProcessDefinitionId());
            System.out.println(h.getName());
        }
    }

    /**
     * 根据流程定义Id查询历史任务信息
     */
    @Test
    public void get222(){
        String pid = "18a6e0fd-4bb7-11eb-afec-6c71d96cc83a";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .processInstanceId(pid)
                .list();
        System.out.println(list.size());
        for (int i = 0; i <list.size() ; i++) {
            HistoricTaskInstance h = list.get(i);
            System.out.println(h.getClaimTime());
            System.out.println(h.getId());
            System.out.println(h.getProcessDefinitionId());
            System.out.println(h.getName());
        }
    }

}
