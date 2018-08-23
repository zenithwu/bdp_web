package com.stylefeng.guns.core.util.jenkins;

import com.offbytwo.jenkins.JenkinsServer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public  final class ProcUtil {

    private static JenkinsServer jenkins;

    static {
        try {
            jenkins = new JenkinsServer(new URI(Constant.URL), Constant.USER,Constant.TOKEN);
        } catch (URISyntaxException e) {
        }
    }


    /***
     * 创建项目
     * @param procName
     * @throws IOException
     */
    public static void createProject(String procName) throws IOException {
        jenkins.createFolder(procName);
    }
    public static void deleteProject(String procName) throws IOException {
        jenkins.deleteJob(procName);
    }
    public static void main(String[] args) throws IOException {
//        ProcUtil.createProject("zenith1");
//        ProcUtil.deleteProject("zenith1");
    }

}
