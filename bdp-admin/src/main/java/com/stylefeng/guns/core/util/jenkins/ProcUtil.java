package com.stylefeng.guns.core.util.jenkins;

import com.offbytwo.jenkins.JenkinsServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public  class ProcUtil {

    private  JenkinsServer jenkins;

    public ProcUtil(String url,String user,String token) {
        try {
            jenkins = new JenkinsServer(new URI(url), user,token);
        } catch (Exception e) {
        }
    }



    /***
     * 创建项目
     * @param procName
     * @throws IOException
     */
    public void createProject(String procName) throws IOException {
        jenkins.createFolder(procName);
    }
    public void deleteProject(String procName) throws IOException {
        jenkins.deleteJob(procName);
    }

    public static void main(String[] args) throws IOException {
//        ProcUtil.createProject("zenith1");
//        ProcUtil.deleteProject("zenith1");
    }

}
