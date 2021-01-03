package com.activiti;

import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test09TaskRuntime {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private TaskRuntime taskRuntime;

    /**
     * 获取当前登录用户任务
     */
    @Test
    public void getInfo111() {
        securityUtil.logInAs("wukong");
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 100));
        List<Task> list = tasks.getContent();
        for (int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            if (task.getAssignee() == null) {
                //候选人为当前登录用户，null的时候需要前端拾取
                System.out.println("Assignee：待拾取任务");
            } else {
                System.out.println(task.getAssignee());
            }

        }
    }

    /**
     * 完成任务
     */
    @Test
    public void getInfo222() {
        securityUtil.logInAs("wukong");
        Task task = taskRuntime.task("instanceId");
        //候选人为Null 需要在前端先拾取任务
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
    }
}
