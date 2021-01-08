import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

//1、如果UEL表达式中流程变量名不存在则报错。
//2、如果UEL表达式中流程变量值为空NLL,流程不按UEL表达式去执行,而流程结束
//3、如果UEL表达式都不符合条件,流程结東
//4、如果连线不设置条件,会走fow序号小的那条线
public class UELTest {
    /**
     *  启动流程实例，动态设置assignee
     */
    @Test
    public void testInfo111(){
        //1.得到ProcessEngine对象
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RuntimeService对象
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //3.设置assignee的取值   用户可以在界面上设置流程的执行人
        Map<String,Object> map = new HashMap<>();
        map.put("assignee0","zhangsan");
        map.put("assignee1","lishi");
        map.put("assignee2","wangwu");

        //4.启动流程实例，同时还要设置流程定义的assignee的值
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holiday2", map);

        //5.输出
        System.out.println(processEngine.getName());
    }
}
