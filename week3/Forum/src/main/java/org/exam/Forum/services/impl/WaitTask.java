package org.exam.Forum.services.impl;

import java.util.concurrent.Callable;

public class WaitTask implements Callable<String> {

	   private String name;

       private Integer sleepTimes;

       public WaitTask(String name, Integer sleepTimes) {
           this.name = name;
           this.sleepTimes = sleepTimes;
       }
       public String call() throws Exception {
    	   System.out.println("intask sleep satrt");
           Thread.sleep(sleepTimes * 1000);
           System.out.println("intask sleep end");
           return "sucess";
       }

}
