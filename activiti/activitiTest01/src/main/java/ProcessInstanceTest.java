import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 启动流程实例，添加进businessKey
 * <p>
 * 本质：act_ru_execution表中的businessKey的字段要存入业务标识
 */
public class ProcessInstanceTest {

    //启动流程实例，添加进businessKey
    @Test
    public void testInfo111() {

        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.启动流程实例,同时还要指定业务标识businessKey  它本身就是请假单的id
        //第一个参数：是指流程定义key
        //第二个参数：业务标识businessKey
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday", "1001");

        //4.输出processInstance相关的属性,取出businessKey使用:processInstance.getBusinessKey()
        System.out.println(processInstance.getBusinessKey());

    }

    //全部流程实例挂起与激活
    @Test
    public void testInfo333() {
//1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.查询流程定义的对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("holiday").singleResult();

        //4.得到当前流程定义的实例是否都为暂停状态
        boolean suspended = processDefinition.isSuspended();

        String processDefinitionId = processDefinition.getId();
        //5.判断
        if (suspended) {
            //说明是暂停，就可以激活操作
            repositoryService.activateProcessDefinitionById(processDefinitionId, true
                    , null);
            System.out.println("流程定义：" + processDefinitionId + "激活");
        } else {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义：" + processDefinitionId + "挂起");
        }
    }

    //单个流程实例挂起与激活
    @Test
    public void testInfo444() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.查询流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("2501").singleResult();

        //4.得到当前流程定义的实例是否都为暂停状态
        boolean suspended = processInstance.isSuspended();

        String processInstanceId = processInstance.getId();
        //5.判断
        if (suspended) {
            //说明是暂停，就可以激活操作
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程：" + processInstanceId + "激活");
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程定义：" + processInstanceId + "挂起");
        }
    }
}
