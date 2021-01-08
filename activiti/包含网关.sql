如果包含网关设置的条件中，流程变量不存在，报错;
org.activiti.engine.ActivitiException:    Unknown    property    used   in    expression:    ${userType=='1'    ||
userType=='2'}
需要在流程启动时设置流程变量  userType
当执行到包含网关：
流程实例执行表：SELECT * FROM act_ru_execution
第一条记录：包含网关分支。
后两条记录：两个分支：常规项体检，抽血化验
当前任务表：ACT_RU_TASK_
上图中，常规项体检，抽血化验都是当前的任务，在并行执行。
如果有一个分支执行到汇聚：
先走到汇聚结点的分支，要等待其它分支走到汇聚。
等所有分支走到汇聚，包含网关就执行完成。
包含网关执行完成，分支和汇聚就从 act_ru_execution删除。
小结：在分支时，需要判断条件，符合条件的分支，将会执行，符合条件的分支最终才进行汇聚。