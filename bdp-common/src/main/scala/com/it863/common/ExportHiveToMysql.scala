package com.it863.common

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

;

object ExportHiveToMysql {

  def main(args: Array[String]): Unit = {

    if (args.length < 5) {
      println(ExportHiveToMysql.getClass.getSimpleName + " HiveQL MysqlTableName mysql_url mysql_user mysql_password")
      System.exit(101)
    }

    val hiveQL = args(0)
    val mysqlTableName = args(1)
    val url = args(2)
    val user = args(3)
    val pass = args(4)

    val conf = new SparkConf().setAppName("ExportHiveToMysql")
    val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
    //    val hiveTableDF = spark.sql(s"select * from ${hiveTableName}")

    // 使用别名方式
    import spark.{sql => hsql}
    val hiveTableDF = hsql(s"${hiveQL}")
    val json = spark.read.json("hdfs://bdp/conf/id.json");


    val r=json.select("id,name,value").collect()(0)


    //设置mysql连接
    val prop = new Properties()
    prop.setProperty("user", user)
    prop.setProperty("password", pass)


    hiveTableDF.write.mode(SaveMode.Overwrite).jdbc(url, mysqlTableName, prop)

    spark.stop()
  }

  def getJobConfig() = {

  }

}
