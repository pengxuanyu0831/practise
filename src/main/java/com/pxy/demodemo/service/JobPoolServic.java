package com.pxy.demodemo.service;


import com.pxy.demodemo.model.JobInfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;


// 框架的线程池
@Service
public class JobPoolServic {
    private static final int THREAD_COUNTS
            = Runtime.getRuntime().availableProcessors();
    // 存放带执行的任务
    private static BlockingQueue<Runnable> taskQueue
            = new ArrayBlockingQueue<>(5000);
    // 线程池，用于存放固定数量的线程
    private static ExecutorService taskExecutor
            = new ThreadPoolExecutor(THREAD_COUNTS,THREAD_COUNTS,500,TimeUnit.SECONDS,taskQueue);
    // 存放工作信息的容器
    private static ConcurrentHashMap<String, JobInfo<?>> jobInfoMap
            = new ConcurrentHashMap<String, JobInfo<?>>();

    public static Map<String,JobInfo<?>> getMap(){
        return jobInfoMap;
    }


}
