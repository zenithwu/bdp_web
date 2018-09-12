package com.stylefeng.guns.core.util;

import com.stylefeng.guns.modular.system.model.ConfConnect;

import java.util.HashMap;
import java.util.Map;

public class JDBCUtil {

    public static Map<String, String> createJDBCUrl(ConfConnect conf, Integer confConnectType) {
        Map<String, String> urlParms = new HashMap<String, String>();
        String url = null;
        if (0 == confConnectType) { // 0: rdbms
            url = String.format("jdbc:mysql://%s:%s/%s", conf.getHost(), conf.getPort(), conf.getDbname());
        } else { // 1: ftp

        }
        urlParms.put("url", url);
        urlParms.put("user", conf.getUsername());
        urlParms.put("password", conf.getPassword());

        return urlParms;
    }

    public Integer executeSQL(String[] sql) {

        return 0;
    }
}
