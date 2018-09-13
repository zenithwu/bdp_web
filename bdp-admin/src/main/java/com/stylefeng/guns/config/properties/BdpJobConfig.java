package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring.bdpjob") //接收application.yml中的myProps下面的属性
public class BdpJobConfig {

    public String getJobconfig() {
        return jobconfig;
    }

    public void setJobconfig(String jobconfig) {
        this.jobconfig = jobconfig;
    }

    public String getJoblib() {
        return joblib;
    }

    public void setJoblib(String joblib) {
        this.joblib = joblib;
    }

    private  String jobconfig;
    private  String joblib;

    public String getZkurl() {
        return zkurl;
    }

    public void setZkurl(String zkurl) {
        this.zkurl = zkurl;
    }

    private  String zkurl;


}
