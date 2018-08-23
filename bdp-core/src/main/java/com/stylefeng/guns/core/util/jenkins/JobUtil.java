package com.stylefeng.guns.core.util.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.FolderJob;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public  class JobUtil {

    private JenkinsServer jenkins;
    private FolderJob proc;

    public JobUtil(String procName) {
        try {
            jenkins = new JenkinsServer(new URI(Constant.URL), Constant.USER,Constant.TOKEN);
            proc=jenkins.getFolderJob(jenkins.getJob(procName)).get();
        } catch (Exception e) {
        }
    }

    /***
     * 在项目中创建任务
     * @param jobName
     */
    public void createJob(String jobName,String desc,String cmd,String params) throws IOException {
        String xml=new JobXML().init(desc,params,cmd).getJobXml();
        jenkins.createJob(proc,jobName,xml);
    }
    /***
     * 在项目中创建任务
     * @param jobName
     */
    public void createJob(String jobName,String desc,String cmd) throws IOException {
        String xml=new JobXML().init(desc,cmd).getJobXml();
        jenkins.createJob(proc,jobName,xml);
    }
    public void updateJobDesc(String jobName,String desc) throws IOException {
        jenkins.getJob(proc,jobName).updateDescription(desc);
    }
    public void setJobCmd(String jobName,String cmd) throws IOException, DocumentException {
        jenkins.updateJob(proc,jobName,new JobXML().init(jenkins.getJobXml(proc,jobName)).setBuilders(cmd).getJobXml(),false);
    }
    public void setJobCronTab(String jobName,String crontab) throws IOException, DocumentException {
        jenkins.updateJob(proc,jobName,new JobXML().init(jenkins.getJobXml(proc,jobName)).setCrontab(crontab).getJobXml(),false);
    }
    public void enableJob(String jobName) throws IOException, DocumentException {
        //因为job在文件夹下 不能直接使用jenkins.enableJob(jobName);
        jenkins.updateJob(proc,jobName,new JobXML().init(jenkins.getJobXml(proc,jobName)).setDisabled("false").getJobXml(),false);
    }

    public void disableJob(String jobName) throws IOException, DocumentException {
        jenkins.updateJob(proc,jobName,new JobXML().init(jenkins.getJobXml(proc,jobName)).setDisabled("true").getJobXml(),false);
    }

    public void runJob(String jobName,Map<String,String> params) throws IOException, DocumentException {
        jenkins.getJob(proc,jobName).build(params);
    }

    public void setDependsJob(String jobName,String depends) throws IOException, DocumentException {
        JobXML jobXML=new JobXML();
        jenkins.updateJob(proc,jobName,jobXML.init(jenkins.getJobXml(proc,jobName)).setUpstreams(depends).getJobXml(),false);
    }

    public boolean ifJobExists(String jobName) throws IOException {
        return (null !=jenkins.getJob(proc,jobName))?true:false;
    }

    public void deleteJob(String jobName) throws IOException {
        jenkins.deleteJob(proc,jobName);
    }


}
