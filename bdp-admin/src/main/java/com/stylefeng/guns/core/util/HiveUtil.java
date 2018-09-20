package com.stylefeng.guns.core.util;


import com.stylefeng.guns.modular.system.model.JobConfig;
import com.stylefeng.guns.modular.system.model.JobTableInfo;
import org.apache.commons.lang.StringUtils;
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

    public  HiveUtil(String serverUrl,String dbName){

        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
            con = DriverManager.getConnection(serverUrl+dbName,"hdfs","hdfs");
//            con = DriverManager.getConnection("jdbc:hive2://bdata003:10000/","hdfs","hdfs");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


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

    public  List<String> getDataBases( String condition) {

        List<String> list=new ArrayList<>();

        try{
            PreparedStatement sta = con.prepareStatement(String.format("show databases  like '*%s*'",condition));
            ResultSet result = sta.executeQuery();
            while(result.next()){
                list.add(result.getString(1));
            }
        } catch(SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    public  List<String> getTablesByDbName(String dbName ) {

        List<String> list=new ArrayList<>();
        try{
            PreparedStatement sta = con.prepareStatement(String.format("show tables in %s ",dbName));
            ResultSet result = sta.executeQuery();
            while(result.next()){
                list.add(result.getString(1));
            }
        } catch(SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    public  List<String> getTablesByDbName(String dbName ,String condition) {

        List<String> list=new ArrayList<>();
        try{
            PreparedStatement sta = con.prepareStatement(String.format("show tables in %s like '*%s*'",dbName,condition));
            ResultSet result = sta.executeQuery();
            while(result.next()){
                list.add(result.getString(1));
            }
        } catch(SQLException e) {
            log.error(e.getMessage());
        }
        return list;
    }

    public  void execute(String script) throws SQLException {
        Statement sta = con.createStatement();
        sta.execute(script);
    }


    public  ResultSet getTableDescByDbName(String dbName,String tableName ) {

        try{
            PreparedStatement sta = con.prepareStatement(String.format("describe %s.%s",dbName,tableName));
            ResultSet result = sta.executeQuery();
            return result;
        } catch(SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    public  List<JobTableInfo> getTablesBycondition(String dbName,String tableName ) {
        dbName=(dbName==null?"":dbName);
        tableName=(tableName==null?"":tableName);

        List<JobTableInfo> reTable=new ArrayList<>();
        List<String > dbs=getDataBases(dbName);
        for (String dName:dbs
             ) {
            List<String> tables=getTablesByDbName(dName,tableName);
            for (String tName:tables
                 ) {
                JobTableInfo jobTableInfo = new JobTableInfo();
                jobTableInfo.setDbName(dName);
                jobTableInfo.setTableName(tName);
                reTable.add(jobTableInfo);
            }
        }
        return reTable;
    }


    public String genTableLocation(String dbName,String tableName) {
        String tabUrl = "default".equals(dbName) ? tableName : dbName + ".db/" + tableName;
        return "/user/hive/warehouse/"+tabUrl;
    }




    public static void main(String[] args) throws ClassNotFoundException {

//        System.out.println(getDataBases());
//        System.out.println(getTablesByDbName("default"));
//        HiveUtil hiveUtil=new HiveUtil("jdbc:hive2://bdata003:10000/");
//        System.out.println(hiveUtil.getTablesBytableName(""));


    }

}
