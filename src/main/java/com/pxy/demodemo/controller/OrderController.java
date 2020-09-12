package com.pxy.demodemo.controller;


import com.pxy.demodemo.OrderTask;
import com.pxy.demodemo.model.TaskResult;
import com.pxy.demodemo.service.JobPoolServic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Controller
public class OrderController {

    private static final String SUCCESS = "suc";
    private static final String FAILURE = "fuilure";

    @Autowired
    private JobPoolServic poolServic;
    @Autowired
    private OrderTask orderTask;

    @RequestMapping("/index")
    public String useReg(){
        return "order";
    }

    @RequestMapping("/submitOrder")
    @ResponseBody
    public String saveUser(@RequestParam("orderNumber") int orderNumber){
        poolServic.registerJob(OrderTask.JOB_NAME,orderNumber,orderTask,10);
        Random r = new Random();
        for(int i = 0; i <= orderNumber; i++){
            poolServic.putTask(OrderTask.JOB_NAME,r.nextInt(1000));
        }
        return SUCCESS;
    }

    @RequestMapping("/queryProgess")
    @ResponseBody
    public String queryProgess(){
        return poolServic.getTaskProcess(OrderTask.JOB_NAME);
    }

    @RequestMapping("/quaryTask")
    @ResponseBody
    public String quaryTask(){
        List<TaskResult<String>> taskResults = poolServic.getTaskDetail(OrderTask.JOB_NAME);
        if(!taskResults.isEmpty()){
            return taskResults.toString();
        }
        return null;
    }


}
