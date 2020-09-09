package com.pxy.demodemo.model;


// 任务信息的实体类
public class JobInfo<R> {
    // 任务名，唯一
    private final String JobName;
    // 工作任务列表
    private final int jobLength;
    // 工作中处理的处理器
    private final ITaskProcesser<?,?> taskProcesser;


    public JobInfo(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser) {
        this.JobName = jobName;
        this.jobLength = jobLength;
        this.taskProcesser = taskProcesser;
    }
}
