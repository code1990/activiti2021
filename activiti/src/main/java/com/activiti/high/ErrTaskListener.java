package com.activiti.high;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ErrTaskListener implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        throw new BpmnError("error");
    }
}
