package com.activiti.high;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TkListener1 implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("执行人1:" + delegateTask.getAssignee());
        //根据当前的执行人查询上级信息 设置上级审批
        //此处把当前执行人设置为下一个审批节点的执行人
        delegateTask.setVariable("delegateAssignee", delegateTask.getAssignee());
    }
}
