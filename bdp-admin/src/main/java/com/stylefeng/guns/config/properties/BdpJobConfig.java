package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring.spark") //接收application.yml中的myProps下面的属性
public class SparkConfig {

    public String getUrl() {
        return url;
    }

    private  String url;
    private  String user;
    private  String token;
    private String rest_url;

}
