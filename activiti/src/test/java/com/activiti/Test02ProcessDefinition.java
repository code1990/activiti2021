package com.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test02ProcessDefinition {

    @Autowired
    private RepositoryService repositoryService;

    //流程定义查询
    @Test
    public void getInfo111(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (int i = 0; i <list.size() ; i++) {
            ProcessDefinition p = list.get(i);
            System.out.println("getCategory>>"+p.getCategory());
            System.out.println("getDeploymentId>>"+p.getDeploymentId());
            System.out.println("getId>>"+p.getId());
            System.out.println("getDescription>>"+p.getDescription());
            System.out.println("getVersion>>"+p.getVersion());
        }
    }

    //删除流程
    @Test
    public void getInfo222(){
        //给定的不是id 是deploymentId
        String deploymentId = "dcf55fd1-4b74-11eb-bb53-6c71d96cc83a";
        // 否认false 保留历史定义信息
        //true 表示一笔勾销 删除与流程所有的信息
        repositoryService.deleteDeployment(deploymentId,true);
    }
}
