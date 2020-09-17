package com.pxy.demodemo.model;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
// 实现了Delay接口，用于存放延时队列的元素
public class ItemVo<T> implements Delayed {
    // 过期时长
    private long activeTime;
    private T data;

    public ItemVo(long expirationTime, T data) {
        this.activeTime = expirationTime*1000+System.currentTimeMillis();
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public long getActiveTime() {
        return activeTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(
                this.activeTime - System.currentTimeMillis(), unit);
        return d;
    }

    @Override
    public int compareTo(Delayed o) {
        long d =(getDelay(TimeUnit.MILLISECONDS)-
                o.getDelay(TimeUnit.MILLISECONDS));
        if(d == 0){
            return 0;
        }else{
            if(d < 0){
                return -1;
            }else {
            return 1;}
        }
    }
}
