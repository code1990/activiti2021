import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 一个完整的业务流程
 * 1.绘制bpmn文件 发布bpmn文件
 * 2.启动流程实例
 * 3.查询待办任务
 * 4.根据任务id完成对应的任务
 */
public class HelloActivity {

    //1.使用流程定义的部署BPMN文件
    //activity表有哪些
//    act_re_deployment  部署信息
//    act_re_procdef     流程定义的一些信息
//    act_ge_bytearray   流程定义的bpmn文件及png文件
    @Test
    public void test111() {
        //1.创建ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.转化出ZipInputStream流对象
        InputStream is = HelloActivity.class.getClassLoader().getResourceAsStream("holidayBPMN.zip");

        //将 inputstream流转化为ZipInputStream流
        ZipInputStream zipInputStream = new ZipInputStream(is);

        //3.进行部署zip
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .name("请假申请单流程")
                .deploy();

        //3.进行部署bpmn
        Deployment deployment2 = repositoryService.createDeployment()
                .addClasspathResource("holiday.bpmn")  //添加bpmn资源
                .addClasspathResource("holiday.png")
                .name("请假申请单流程")
                .deploy();

        //4.输出部署的一些信息
        System.out.println(deployment.getName());
        System.out.println(deployment.getId());

    }

    /**
     * 2.启动流程实例:
     * 前提是先已经完成流程定义的部署工作
     * <p>
     * 背后影响的表：
     * act_hi_actinst     已完成的活动信息
     * act_hi_identitylink   参与者信息
     * act_hi_procinst   流程实例
     * act_hi_taskinst   任务实例
     * act_ru_execution   执行表
     * act_ru_identitylink   参与者信息
     * act_ru_task  任务
     */
    @Test
    public void test222() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RunService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.创建流程实例  流程定义的key需要知道 holiday
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday");

        //4.输出实例的相关信息
        System.out.println("流程部署ID" + processInstance.getDeploymentId());//null
        System.out.println("流程定义ID" + processInstance.getProcessDefinitionId());//holiday:1:4
        System.out.println("流程实例ID" + processInstance.getId());//2501
        System.out.println("活动ID" + processInstance.getActivityId());//null
    }

    //    查询当前用户的任务列表
    @Test
    public void testInfo333() {
//1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.根据流程定义的key,负责人assignee来实现当前用户的任务列表查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("wangwu")
                .singleResult();
        //3.根据流程定义的key,负责人assignee来实现当前用户的任务列表查询
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("zhangsan")
                .list();
        //4.任务列表的展示
        System.out.println("流程实例ID:" + task.getProcessInstanceId());
        System.out.println("任务ID:" + task.getId());  //5002
        System.out.println("任务负责人:" + task.getAssignee());
        System.out.println("任务名称:" + task.getName());

        //4.任务列表的展示
        for (Task task1 : taskList) {
            System.out.println("流程实例ID:" + task1.getProcessInstanceId());
            System.out.println("任务ID:" + task1.getId());
            System.out.println("任务负责人:" + task1.getAssignee());
            System.out.println("任务名称:" + task1.getName());
        }
    }

    /**
     * 处理当前用户的任务
     * 背后操作的表：
     *   act_hi_actinst
     act_hi_identitylink
     act_hi_taskinst
     act_ru_identitylink
     act_ru_task
     */
    @Test
    public void test444(){
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.查询当前用户的任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("holiday")
                .taskAssignee("wangwu")
                .singleResult();

        //4.处理任务,结合当前用户任务列表的查询操作的话,任务ID:task.getId()
        taskService.complete(task.getId());

        //5.输出任务的id
        System.out.println(task.getId());
    }
}
