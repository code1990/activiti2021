import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

//任务相当事件包括
//Create:任务创建后触发
//Assignment:任务分配后触发
//Delete:任务完成后触发
//AlL所有事件发生都触发
//
//定义任务监听类，且类必须实现 org.activiti.engine.delegate.TaskListener接口
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee("zhangsan");
    }
}
