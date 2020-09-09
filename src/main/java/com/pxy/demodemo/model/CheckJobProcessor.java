package com.pxy.demodemo.model;

import com.pxy.demodemo.service.JobPoolServic;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

public class CheckJobProcessor {
    // 存放未执行任务的队列
    private static DelayQueue<ItemVo<String>> queue
            = new DelayQueue<>();
    // 处理过期任务的方法
    private static class otTask implements Runnable{
        private static DelayQueue<ItemVo<String>> queue
                = CheckJobProcessor.queue;
        // 缓存的工作信息
        private static Map<String,JobInfo<?>> jobInfoMap
                = JobPoolServic.getMap();



        @Override
        public void run() {
        while(true){
            try{
                ItemVo<String> itemVo = queue.take();
                String jobName = (String)itemVo.getData();
                jobInfoMap.remove(jobName);
                System.out.println(jobName + "过期了，已经清除");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
        // 放入队列的任务，经过一定时间，会被清除出去
        public void putJob(String jobName,long expireTime){
            ItemVo<String> itemVo = new ItemVo<>(expireTime,jobName);
            queue.offer(itemVo);
            System.out.println(jobName + "任务已经过期");
        }
    }


        static{
            Thread thread = new Thread(new otTask());
            thread.setDaemon(true);
            thread.start();
            System.out.println("开启过期检查的守护线程......");
        }
}





