import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.jupiter.api.Test;

//测试25张数据表的生成
public class ActivitiTest {

    @Test
    public void testGenTable() {
        //使用默认的方式创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
    }

    @Test
    public void testGenTable2() {
        //条件：1.activiti配置文件名称：activiti.cfg.xml
        // 2.bean的id="processEngineConfiguration"
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration01");

        ProcessEngine processEngine = configuration.buildProcessEngine();

        System.out.println(processEngine);
    }
//    数据库表的命名规则
//    Activiti的表都以ACT_开头。第二部分是表示表的用途的两个字母标识。用途也和服务的API对
//            应
//    ACT_RE_*:'RE'表示 repositoⅳy。这个前缀的表包含了流程定义和流程静态资源(图片,
//                                                    规则,等等)。
//    ACT_RU*:'RU表示 runtime。这些运行时的表,包含流程实例,任务,变量,异步任务
//    等运行中的数据。 Activiti只在流程实例执行过程中保存这些数据,在流程结束时就会删
//    除这些记录。这样运行时表可以一直很小速度很快。
//    ACT_HI_*:'HI'表示 history。这些表包含历史数据,比如历史流程实例,变量,任务等等
//    ACT_GE_*:E表示 genera1。通用数据,用于不同场景下。
}
