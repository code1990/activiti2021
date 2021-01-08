SELECT * FROM act_ru_task #任务执行表，记录当前执行的任务，由于该任务当前是组任务，所有
assignee为空，当拾取任务后该字段就是拾取用户的   id
SELECT * FROM act_ru_identitylink #任务参与者，记录当前参考任务用户或组，当前任务如果设置
了候选人，会向该表插入候选人记录，有几个候选就插入几个
于 act_ru_identitylink对应的还有一张历史表  act_hi_identitylink，向 act_ru_identitylink插入记录的同
时也会向历史表插入记录。任务完成