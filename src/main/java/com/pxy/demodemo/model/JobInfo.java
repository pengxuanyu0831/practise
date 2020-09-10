package com.pxy.demodemo.model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/*任务信息的实体类
* 抽象了对用户工作的封装
* 标识本批次，相同任务task的集合 */
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
    private LinkedBlockingDeque<TaskResult<R>> taskResultQueue;

    // 任务超时时间
    private final long expireTime;

    private static CheckJobProcessor checkJobProcessor = CheckJobProcessor.getInstance();


    public JobInfo(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser, long expireTime) {
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.taskProcesser = taskProcesser;
        this.expireTime = expireTime;
        successCount = new AtomicInteger();
        taskProcesserCount =new AtomicInteger();
        taskResultQueue = new LinkedBlockingDeque<TaskResult<R>>(jobLength);

    }

    public int getSuccessCount() {
        return successCount.get();
    }
    public int getTaskProcesserCount() {
        return taskProcesserCount.get();
    }
    public int getFailureCount(){
        return taskProcesserCount.get() - successCount.get();
    }
    public int getJobLength(){
        return jobLength;
    }
    public String getCurrentProcesser(){
        return "Success:" + getSuccessCount() + "\n Failure:" + getFailureCount()
                +"\n Total:" + getTaskProcesserCount();
    }


    // 提供工作中，每个任务的处理结果
    private List<TaskResult<R>> getTaskDetail(){
        List<TaskResult<R>> taskResultsList = new LinkedList<>();
        TaskResult<R>taskResult;
        while ((taskResult = taskResultQueue.pollFirst())!= null){
            taskResultsList.add(taskResult);

        }
        return taskResultsList;
     }
    /*每个任务处理完成后，记录任务的处理结果*/
     public void addTaskResult(TaskResult<R> taskResult){
         // 如果任务处理成功
        if(TaskResultEm.Success.equals(taskResult.getReturnValue())){
            successCount.incrementAndGet();
        }
        taskProcesserCount.getAndIncrement();
        taskResultQueue.addLast(taskResult);
        if(getTaskProcesserCount() == jobLength){
            checkJobProcessor.putJob(jobName,expireTime);
        }

     }

}
