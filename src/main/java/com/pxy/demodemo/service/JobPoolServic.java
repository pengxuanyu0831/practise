package com.pxy.demodemo.service;


import com.pxy.demodemo.model.ITaskProcesser;
import com.pxy.demodemo.model.JobInfo;
import com.pxy.demodemo.model.TaskResult;
import com.pxy.demodemo.model.TaskResultEm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


// 框架的线程池
@Service
public class JobPoolServic {
    private static final int THREAD_COUNTS
            = Runtime.getRuntime().availableProcessors();
    // 存放带执行的任务
    private static BlockingQueue<Runnable> taskQueue
            // arraryBlockingQueue  数组结构，有界阻塞队列，需指定容量、是否公平，默认非公平
            = new ArrayBlockingQueue<>(5000);
    // 线程池，用于存放固定数量的线程
    // 有界队列
    private static ExecutorService taskExecutor
            = new ThreadPoolExecutor(THREAD_COUNTS,THREAD_COUNTS,500,TimeUnit.SECONDS,taskQueue);
    // 存放工作信息的容器 key-job名称 value-job的信息
    private static ConcurrentHashMap<String, JobInfo<?>> jobInfoMap
            = new ConcurrentHashMap<String, JobInfo<?>>();

    public static Map<String,JobInfo<?>> getMap(){
        return jobInfoMap;
    }


    // 以单例模式启动，这是线程池，所以单例启动就行
/*    private JobPoolServic(){};

    private static class JobPoolServicHolder{
        public static JobPoolServic jobPoolServic = new JobPoolServic();
    }

    public static JobPoolServic getInstance(){
        return JobPoolServicHolder.jobPoolServic;
    }*/

    // 对工作中的任务进行包装，丢给线程池去执行
    // 对处理完的任务进行保存，以供查询
    public static class PoolTasks<T ,R> implements Runnable{
        public JobInfo<R> jobInfo;
        private T proessData;

        public PoolTasks(JobInfo<R> jobInfo, T proessData) {
            this.jobInfo = jobInfo;
            // 业务数据
            this.proessData = proessData;
        }

        @Override
        public void run() {
            R r = null;
            ITaskProcesser<T ,R > taskProcesser = (ITaskProcesser<T ,R > )jobInfo.getTaskProcesser();
            TaskResult<R> result = null;
            try {

                // 调用接口实例去对提交的业务数据进行处理
                result = taskProcesser.taskExecute(proessData);
                if(result == null){
                    result = new TaskResult<R>(TaskResultEm.Exception,r,"reslut is null");
                }
                if(result.getReturnValue() == null ){
                    if(result.getReson() == null ){
                        result = new TaskResult<R>(TaskResultEm.Exception,r,"reslut is null");

                    }else{
                        result = new TaskResult<R>(TaskResultEm.Exception,r,"reslut is null" + result.getReson());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = new TaskResult<R>(TaskResultEm.Exception,r,"reslut is null"+ e.getMessage());
            }finally {
                jobInfo.addTaskResult(result);
            }
        }



    }

    public <R> void registerJob(String jobName,int jobLength,ITaskProcesser<?, ?> taskProcesser,long expireTime){
        JobInfo<R> jobInfo = new JobInfo<>(jobName,jobLength,taskProcesser,expireTime);
        if(jobInfoMap.putIfAbsent(jobName,jobInfo) != null){
            throw new RuntimeException("该任务已经注册过了");
        }
    }

    private <R> JobInfo<R> getJob(String jobName){
        JobInfo<R> jobInfo = (JobInfo<R>) jobInfoMap.get(jobName);
        if (null==jobInfo)
            throw new RuntimeException(jobName+"是非法任务！");
        return jobInfo;
    }

    public <T,R > void  putTask(String jobName,T t){
        JobInfo<R> jobInfo = getJob(jobName);
        PoolTasks<T,R> poolTasks = new PoolTasks<>(jobInfo,t);
        taskExecutor.execute(poolTasks);

    }
    // 获取每个任务的信息
    public <R> List<TaskResult<R>> getTaskDetail(String jobName){
        JobInfo<R> jobInfo = getJob(jobName);
        return jobInfo.getTaskDetail();
    }

    // 获取整体任务的信息
    public <R> String getTaskProcess(String jobName){
        JobInfo<R> jobInfoPro = getJob(jobName);
        return jobInfoPro.getCurrentProcesser();
    }






}
