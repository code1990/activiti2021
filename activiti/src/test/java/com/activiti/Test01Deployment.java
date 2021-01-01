package com.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@SpringBootTest
public class Test01Deployment {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    /**
     * 使用文件直接发布流程
     */
    @Test
    public void getInfo111() {
        String fileName = "BPMN/part01Deployment.bpmn";
        String pngName = "BPMN/part01Deployment.png";
        //流程发布的时候可以携带bpmn文件+缩略图
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(fileName)
                .addClasspathResource(pngName)
                .name("流程发布定义BPMN")
                .deploy();
        System.out.println(deployment.getName());
    }

    /**
     * 使用压缩的方式发布流程
     */
    @Test
    public void getInfo222() {
        String path = "BPMN/BPMN_V2.zip";
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(path);
        // 使用压缩包来发布流程图 流程图会被解压为bpmn文件 png文件
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("流程发布定义BPMNV2")
                .deploy();
        System.out.println(deployment.getName());

    }

    /**
     * 查看发布过的流程
     *
     */
    @Test
    public void getInfo333(){
        List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
        for (int i = 0; i <deployments.size() ; i++) {
            Deployment d = deployments.get(i);
            System.out.println(d.getKey());
            System.out.println(d.getName());
            System.out.println(d.getCategory());
            System.out.println(d.getDeploymentTime());
            System.out.println(d.getId());
        }
    }

}
