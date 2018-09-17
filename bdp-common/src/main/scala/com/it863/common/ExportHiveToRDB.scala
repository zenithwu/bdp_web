package com.it863.common

import java.util.Properties

import com.it863.common.util.JdbcUtil._
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object ExportHiveToRDB {

  def main(args: Array[String]): Unit = {
    val jobid = args(0)
    val timehour = args(1)

    val hdfsPath = "hdfs:///bdp/jobconfig/jobid_" + jobid + ".json"

    val conf = new SparkConf().setAppName("ExportHiveToMysql")
    val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
    val row = spark.read.json(hdfsPath).first()
    val jobConfig = row.getValuesMap(row.schema.fieldNames)
    // 获取参数
    val hiveQL = jobConfig.getOrElse("input_input_content", "")
    val saveMode = jobConfig.getOrElse("output_write_model", "overwrite")
    val jdbcUser = jobConfig.getOrElse("username", "")
    val jdbcPass = jobConfig.getOrElse("password", "")
    val jdbcHost = jobConfig.getOrElse("host", "")
    val jdbcPort = jobConfig.getOrElse("port", "3306")
    val dbType = jobConfig.getOrElse("type", "").toLowerCase
    val driver = jobConfig.getOrElse("driver", "")
    val dbName = jobConfig.getOrElse("dbname", "")
    val preSql = jobConfig.getOrElse("output_pre_statment", "")
    val targetTable = jobConfig.getOrElse("output_table_name", "")

    val url = urlFormat(dbType, jdbcHost, jdbcPort, dbName)

    // 使用别名方式
    import spark.{sql => hsql}
    val hiveDF = hsql(s"${hiveQL}")


    // 执行前置sql
    if (preSql != "") {
      runSQL(driver, preSql, url, jdbcUser, jdbcPass)
    }


    //设置mysql连接
    val prop = new Properties()
    prop.setProperty("user", jdbcUser)
    prop.setProperty("password", jdbcPass)

    val sm = if (saveMode == "append") SaveMode.Append else SaveMode.Overwrite
    hiveDF.write.mode(sm).jdbc(url, targetTable, prop)

    spark.stop()
  }

}
