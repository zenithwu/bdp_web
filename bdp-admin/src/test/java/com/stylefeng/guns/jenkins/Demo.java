package com.stylefeng.guns.jenkins;

import com.stylefeng.guns.core.util.jenkins.JobUtil;
import org.dom4j.DocumentException;

import java.io.IOException;

public class Demo {

        public static void main(String[] args) throws IOException, DocumentException {

//        ProcUtil.createProject("zenith");
            JobUtil jobUtil=new JobUtil("zenith");

//
//        jobUtil.deleteJob("test1");
//        jobUtil.deleteJob("test2");
//        jobUtil.deleteJob("test3");
//        jobUtil.deleteJob("test4");
//
//        jobUtil.createJob("test1","test1","echo test1");
//        jobUtil.createJob("test2","test2","echo test2");
//
//        jobUtil.createJob("test3","test3","echo test3");
////
//        jobUtil.createJob("test4","test4","echo test4");
//
//        jobUtil.setDependsJob("test2","test1");
//        jobUtil.setDependsJob("test3","test1");
//        jobUtil.setDependsJob("test4","test2,test3");
//
//
//        jobUtil.setJobCmd("test1","echo test1\n echo ${time_hour}");
//        jobUtil.setJobCmd("test2","echo test2\n echo ${time_hour}\n sleep 60");
//        jobUtil.setJobCmd("test3","echo test3\n echo ${time_hour}\n sleep 10");
//        jobUtil.setJobCmd("test4","echo test4\n echo ${time_hour}");
//        jobUtil.updateJobDesc("test1","test1desc");
//        jobUtil.disableJob("test1");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        jobUtil.enableJob("test1");


//        jobUtil.setJobCronTab("test1","1 1 * * *");
//        Map<String,String > param=new HashMap<>();
//        param.put("time_hour","2018-08-23");
//        jobUtil.runJob("test1",param);

            System.out.println();
        }

    }