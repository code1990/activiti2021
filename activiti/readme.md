关于processEngineConfiguration中的    databaseSchemaUpdate参数，通过此参数设计    activiti
数据表的处理策略，参数如下：
false（默认）：检查数据库表的版本和依赖库的版本，如果版本不匹配就抛出异常。
true:构建流程引擎时，执行检查，如果需要就执行更新。如果表不存在，就创建。
create-drop:构建流程引擎时创建数据库表，关闭流程引擎时删除这些表。
drop-create：先删除表再创建表。
create:构建流程引擎时创建数据库表，关闭流程引擎时不删除这些表。

数据库表的命名规则
Activiti的表都以ACT_开头。第二部分是表示表的用途的两个字母标识。用途也和服务的API对
应。
  ACT_RE_*: 'RE'表示repository。这个前缀的表包含了流程定义和流程静态资源（图片，
规则，等等）。
  ACT_RU_*: 'RU'表示runtime 。这些运行时的表，包含流程实例，任务，变量，异步任务，
等运行中的数据。 Activiti只在流程实例执行过程中保存这些数据，在流程结束时就会删
除这些记录。这样运行时表可以一直很小速度很快。
  ACT_HI_*: 'HI'表示history。这些表包含历史数据，比如历史流程实例，变量，任务等
等。
  ACT_GE_*:  GE表示general。通用数据，用于不同场景下。


Service总览
RepositoryServic    eactiviti的资源管理类
RuntimeService  activiti的流程运行管理类
TaskService activiti的任务管理类
HistoryService  activiti的历史管理类
ManagerService  activiti的引擎管理类
注：红色标注为常用 service

Connection—连接
Event---事件
Task---任务
Gateway---网关
Container—容器
Boundary event—边界事件
Intermediate event- -中间事件

任务相当事件包括
Create:任务创建后触发
Assignment:任务分配后触发
Delete:任务完成后触发
AlL所有事件发生都触发

定义任务监听类，且类必须实现 org.activiti.engine.delegate.TaskListener接口

如果将 pojo存储到流程变量中，必须实现序列化接口  serializable，为了防止由于新增字段无
法反序列化，需要生成 serialVersionUID

组任务办理流程
第一步：查询组任务
指定候选人，查询该候选人当前的待办任务。
候选人不能办理任务。
第二步：拾取(claim)任务
该组任务的所有候选人都能拾取。
将候选人的组任务，变成个人任务。原来候选人就变成了该任务的负责人。
***如果拾取后不想办理该任务？
需要将已经拾取的个人任务归还到组里边，将个人任务变成了组任务。
第三步：查询个人任务
查询方式同个人任务部分，根据 assignee查询用户负责的个人任务。
第四步：办理个人任务


即使有两个分支条件都为  true，排他网关也会只选择一条分支去执行 **id更小的值分支去走**

**都不成立 直接报错**

**包含网关可以看做是排他网关和并行网关的结合体**。和排他网关一样，你可以在外出顺序流上
定义条件，包含网关会解析它们。但是主要的区别是包含网关可以选择多于一条顺序流，这和并行
网关一样。
包含网关的功能是基于进入和外出顺序流的：

分支：
所有外出顺序流的条件都会被解析，结果为 true的顺序流会以并行方式继续执行


整合springboot的说明
从官方demo加入如下的代码
SecurityUtil.java
DemoApplicationConfiguration.java

resource下的process文件夹下的bpmn文件会被自动部署

【No1175】Activiti7精讲Java通用工作流开发实战
【No1015】Activiti7工作流引擎视频教程
