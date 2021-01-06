import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;

import java.util.List;

public class ProcessDefinitionTest {

    //查询流程定义信息
    @Test
    public void testInfo111() {
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.创建RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.得到ProcessDefinitionQuery对象,可以认为它就是一个查询器
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //4.设置条件，并查询出当前的所有流程定义   查询条件：流程定义的key=holiday
        //orderByProcessDefinitionVersion() 设置排序方式,根据流程定义的版本号进行排序
        List<ProcessDefinition> list = processDefinitionQuery.processDefinitionKey("holiday")
                .orderByProcessDefinitionVersion()
                .desc().list();

        //5.输出流程定义信息
        for (ProcessDefinition processDefinition : list) {
            System.out.println("流程定义ID：" + processDefinition.getId());
            System.out.println("流程定义名称：" + processDefinition.getName());
            System.out.println("流程定义的Key：" + processDefinition.getKey());
            System.out.println("流程定义的版本号：" + processDefinition.getVersion());
            System.out.println("流程部署的ID:" + processDefinition.getDeploymentId());

        }
    }

    //删除已经部署的流程定义
    //背后影响的表:
    // * act_ge_bytearray
    //   act_re_deployment
    //   act_re_procdef
    @Test
    public void testInfo222() {
        /**
         * 注意事项：
         *     1.当我们正在执行的这一套流程没有完全审批结束的时候，此时如果要删除流程定义信息就会失败
         *     2.如果公司层面要强制删除,可以使用repositoryService.deleteDeployment("1",true);
         *     //参数true代表级联删除，此时就会先删除没有完成的流程结点，最后就可以删除流程定义信息  false的值代表不级联
         *
         */
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.创建RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //3.执行删除流程定义  参数代表流程部署的id
        repositoryService.deleteDeployment("1");

    }


}
