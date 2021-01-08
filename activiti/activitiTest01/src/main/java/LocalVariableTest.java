import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.junit.Test;

public class LocalVariableTest {

    @Test
    public void setLocalVariableByTaskId() {
        //1.得到ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //当前待办任务id
        String taskId = "1404";
        TaskService taskService = processEngine.getTaskService();
        Holiday holiday = new Holiday();
        holiday.setNum(3F);
        //通过任务设置流程变量
        taskService.setVariableLocal(taskId, "holiday", holiday);
        //一次设置多个值
        //taskService.setVariablesLocal(taskId, variables)
    }
}