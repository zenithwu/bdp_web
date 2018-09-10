package com.stylefeng.guns.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HiveUtil {


    private  Logger log = LoggerFactory.getLogger(HiveUtil.class);
    private  Connection con;



    public  HiveUtil(String serverUrl){

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            con = DriverManager.getConnection(serverUrl,"hdfs","hdfs");
//            con = DriverManager.getConnection("jdbc:hive2://bdata003:10000/","hdfs","hdfs");
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
    public  List<String> getDataBases( ) {

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
    public  List<String> getTablesByDbName(String dbName ) {

        List<String> list=new ArrayList<>();
        try{
            PreparedStatement sta = con.prepareStatement(String.format("show tables in %s",dbName));
            ResultSet result = sta.executeQuery();
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
//        System.out.println(getTablesByDbName("default"));


    }

}
