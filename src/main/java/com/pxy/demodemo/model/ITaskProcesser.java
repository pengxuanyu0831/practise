package com.pxy.demodemo.model;

// 执行用户的业务方法
public interface ITaskProcesser<T ,R > {
    TaskResult<R>taskExecute(T data);
}
