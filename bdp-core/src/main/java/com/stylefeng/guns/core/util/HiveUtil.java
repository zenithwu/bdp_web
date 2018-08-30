package com.stylefeng.guns.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HiveUtil {


    private static Logger log = LoggerFactory.getLogger(HiveUtil.class);
    private static Connection con;

    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            con = DriverManager.getConnection("jdbc:hive2://bdata003:10000/","hdfs","hdfs");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 删除目录
     *
     * @author wuzhanwei
     * @Date 2018/08/30 下午4:15
     */
    public static List<String> getDataBases( ) {

        List<String> list=new ArrayList<>();

        try{
            PreparedStatement sta = con.prepareStatement("show databases");
            ResultSet result = sta.executeQuery();
            while(result.next()){
                list.add(result.getString(1));
            }
        } catch(SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * 删除目录
     *
     * @author wuzhanwei
     * @Date 2018/08/30 下午4:15
     */
    public static List<String> getTablesByDbName(String dbName ) {

        List<String> list=new ArrayList<>();
        try{
            PreparedStatement sta = con.prepareStatement("use "+dbName);
            ResultSet result = sta.executeQuery("show tables");
            while(result.next()){
                list.add(result.getString(1));
            }
        } catch(SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    public static void main(String[] args) throws ClassNotFoundException {

//        System.out.println(getDataBases());
        System.out.println(getTablesByDbName("default"));


    }

}
