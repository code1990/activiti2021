当执行到并行网关数据库跟踪如下：
当前任务表：SELECT * FROM act_ru_task #当前任务表
上图中：有两个(多个)任务当前执行。
通过流程实例执行表：SELECT * FROM act_ru_execution #流程实例的执行表


上图中，说明当前流程实例有多个分支(两个)在运行。
对并行任务的执行：
并行任务执行不分前后，由任务的负责人去执行即可。
当完成并任务中一个任务后：
已完成的任务在当前任务表 act_ru_task_已被删除。
在流程实例执行表：SELECT * FROM act_ru_execution有中多个分支存在且有并行网关的汇聚结点。
有并行网关的汇聚结点：说明有一个分支已经到汇聚，等待其它的分支到达。
当所有分支任务都完成，都到达汇聚结点后：
流程实例执行表：SELECT * FROM act_ru_execution，执行流程实例不存在，说明流程执行结束。
总结：所有分支到达汇聚结点，并行网关执行完成。