package com.pxy.demodemo.model;

public interface ITaskProcesser<T ,R > {
    TaskResult<R>taskExecute(T data);
}
