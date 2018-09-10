package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring.jenkins") //接收application.yml中的myProps下面的属性
public class JenkinsConfig {

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private  String url;
    private  String user;
    private  String token;
    private String rest_url;

    public String getRest_url() {
        return rest_url;
    }

    public void setRest_url(String rest_url) {
        this.rest_url = rest_url;
    }
}
