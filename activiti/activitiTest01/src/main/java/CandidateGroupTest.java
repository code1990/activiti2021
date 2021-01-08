import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class CandidateGroupTest {
    //1.部署流程定义
    //2.启动流程实例
    //3.填写请假单的任务要执行完成
    //4.查询候选用户的组任务
    //5.测试zhangsan用户，来拾取组任务
    //抽取任务的过程就是将候选用户转化为真正任务的负责人（让任务的assignee有值）

    //任务拾取完毕以后 把任务转交给其他候选人处理

    //候选人完成自己的任务
    @Test
    public void testInfo111() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService对象
        TaskService taskService = processEngine.getTaskService();

        //3.设置一些参数，流程定义的key,候选用户
        String key = "myProcess_1";
        String candidate_users = "zhangsan";

        //4.执行查询
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidate_users)//设置候选用户
                .singleResult();

        //1.拾取任务
        if (task != null) {
            taskService.claim(task.getId(), candidate_users);
            //第一个参数任务ID,第二个参数为具体的候选用户名
            System.out.println("任务拾取完毕!");
        }
        //5.判断是否有这个任务
        if (task != null) {
            taskService.setAssignee(task.getId(), "lisi");
            //交接任务为lisi  ,交接任务就是一个候选人拾取用户的过程
            System.out.println("交接任务完成~!");
        }
        //5.执行当前的任务
        if (task != null) {
            taskService.complete(task.getId());
            System.out.println("任务执行完毕!");
        }
    }


}
