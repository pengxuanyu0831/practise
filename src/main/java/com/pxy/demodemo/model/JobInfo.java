package com.pxy.demodemo.model;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

// 任务信息的实体类
public class JobInfo<R> {
    // 任务名，唯一
    private final String jobName;
    // 工作任务列表
    private final int jobLength;
    // 工作中处理的处理器
    private final ITaskProcesser<?,?> taskProcesser;

    private AtomicInteger successCount;
    private AtomicInteger taskProcesserCount;

    // 存放每个任务的处理结果
    private LinkedBlockingQueue<TaskResult<R>> taskResultQueue;

    // 任务超时时间
    private final long expireTime;




    public JobInfo(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser) {
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.taskProcesser = taskProcesser;
    }


}
