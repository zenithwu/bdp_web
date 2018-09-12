package com.stylefeng.guns.core.util;


import com.stylefeng.guns.modular.system.model.JobTableInfo;
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


    public  List<JobTableInfo> getTablesBytableName(String contition ) {

        List<JobTableInfo> reTable=new ArrayList<>();

        List<String > dbs=getDataBases();
        for (String dbName:dbs
             ) {
            List<String> tables=getTablesByDbName(dbName);

            for (String tableName:tables
                 ) {
                if(contition==null){
                    contition="";
                }
                if(tableName.matches("^.*"+contition+".*$")){
                    JobTableInfo jobTableInfo=new JobTableInfo();
                    jobTableInfo.setDbName(dbName);
                    jobTableInfo.setTableName(tableName);
                    jobTableInfo.setDesc("………………");
                    reTable.add(jobTableInfo);
                }
            }

        }
        return reTable;
    }




    public static void main(String[] args) throws ClassNotFoundException {

//        System.out.println(getDataBases());
//        System.out.println(getTablesByDbName("default"));
//        HiveUtil hiveUtil=new HiveUtil("jdbc:hive2://bdata003:10000/");
//        System.out.println(hiveUtil.getTablesBytableName(""));


    }

}
