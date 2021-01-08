设置流程变量会在当前执行流程变量表插入记录，同时也会在历史流程变量表也插入记录。
SELECT * FROM act_ru_variable #当前流程变量表
记录当前运行流程实例可使用的流程变量，包括  global和  local变量
Id_：主键
Type_：变量类型
Name_：变量名称
Execution_id_：所属流程实例执行 id，global和  local变量都存储
Proc_inst_id_：所属流程实例 id，global和  local变量都存储
Task_id_：所属任务 id，local变量存储
Bytearray_：serializable类型变量存储对应 act_ge_bytearray表的   id
Double_：double类型变量值
Long_：long类型变量值
Text_：text类型变量值
SELECT * FROM act_hi_varinst    #历史流程变量表
记录所有已创建的流程变量，包括  global和  local变量
字段意义参考当前流程变量表。