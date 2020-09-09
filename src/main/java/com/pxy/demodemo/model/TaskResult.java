package com.pxy.demodemo.model;

/* 任务结果处理的结果类*/


public class TaskResult<R> {
    // 任务处理状态
    private final TaskResultEm taskResultEm;
    // 任务处理返回值
    private final R returnValue;
    // 任务失败原因
    private final String reson;

    // 任务失败了，走这个方法
    public TaskResult(TaskResultEm taskResultEm ,R returnValue,String reson) {
        super();
        this.taskResultEm = taskResultEm;
        this.returnValue = returnValue;
        this.reson = reson;
    }
    public TaskResult(TaskResultEm taskResultEm,R returnValue) {
        super();
        this.taskResultEm = taskResultEm;
        this.returnValue = returnValue;
        this.reson = "Success";
    }

    public TaskResultEm taskResultEm(){
        return taskResultEm;
    }

    public String getReson() {
        return reson;
    }

    public R getReturnValue() {
        return returnValue;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "taskResultEm=" + taskResultEm +
                ", returnValue=" + returnValue +
                ", reson='" + reson + '\'' +
                '}';
    }
}
