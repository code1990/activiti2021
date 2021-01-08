import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GlobalVariableTest {
    //完成任务  zhangsan  -----lishi----判断流程变量的请假天数,1天----分支：人事经理存档(zhaoliu)
    public static void main3(String[] args) {
        //1.得到ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到TaskService
        TaskService taskService = processEngine.getTaskService();

        //3.查询当前用户是否有任务
        String key = "myProcess_1";
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee("zhaoliu").singleResult();

        //4.判断task!=null,说明当前用户有任务
        if(task!=null){
            taskService.complete(task.getId());
            System.out.println("任务执行完毕");
        }

    }


    //启动流程实例，同时还要设置流程变量的值
    // act_ge_bytearray
    // act_ru_variable
    public static void main(String[] args) {
        //1.得到ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.得到RuntimeService
        RuntimeService runtimeService =  processEngine.getRuntimeService();

        //3.流程定义的key问题   myProcess_1
        String key = "myProcess_1";
        Map<String ,Object> map = new HashMap<>();

        Holiday holiday = new Holiday();
        holiday.setNum(5F);
        map.put("holiday",holiday);

        //4.启动流程实例，并且设置流程变量的值
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, map);

        //5.输出实例信息
        System.out.println(processInstance.getName());
        System.out.println(processInstance.getProcessDefinitionId());

    }


}
/**
 * 请假实体类:
 *    注意POJO类型，一定要实现Serializable接口，否则在存储这个pojo时就会报异常
 */
class Holiday implements Serializable {
    private Integer id;
    private String holidayName;//申请人的名字
    private Date beginDate;//开始时间
    private Date endDate;//结束日期
    private Float num;//请假天数
    private String reason;//事由
    private String type;//请假类型

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
