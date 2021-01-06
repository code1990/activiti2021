package com.activiti.high;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class TkListener2 implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("执行人2:" + delegateTask.getAssignee());
        //动态设置执行人
        delegateTask.setAssignee("wukong");
    }
}
