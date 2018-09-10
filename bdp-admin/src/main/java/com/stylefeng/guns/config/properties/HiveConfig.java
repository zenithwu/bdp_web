package com.stylefeng.guns.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.hive")
public class HiveConfig {
    private String beeline;
    private String url;

    public String getBeeline() {
        return beeline;
    }

    public void setBeeline(String beeline) {
        this.beeline = beeline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
