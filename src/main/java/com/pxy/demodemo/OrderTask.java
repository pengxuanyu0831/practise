package com.pxy.demodemo;


import com.pxy.demodemo.model.ITaskProcesser;
import com.pxy.demodemo.model.TaskResult;
import com.pxy.demodemo.model.TaskResultEm;
import com.pxy.demodemo.utils.SleepTools;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OrderTask implements ITaskProcesser<Integer,Integer> {
    public static final String JOB_NAME = "疫情信息提交";


    @Override
    public TaskResult<Integer> taskExecute(Integer data) {
        Random r = new Random();
        int flag = r.nextInt(500);
        SleepTools.ms(flag);
        if(flag <= 300){
            Integer returnValue = data.intValue() + flag;
            return new TaskResult<Integer>(TaskResultEm.Success,returnValue);
        }else if(flag > 300 && flag <= 500){
            return new TaskResult<Integer>(TaskResultEm.Failure,-1,"task failure");
        }else{
            try{
                throw new RuntimeException("例外发生了");
            }catch (Exception e){
                return new TaskResult<Integer>(TaskResultEm.Exception,-1,e.getMessage());
            }
        }


    }
}
