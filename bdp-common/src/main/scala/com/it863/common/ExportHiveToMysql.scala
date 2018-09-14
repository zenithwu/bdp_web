package com.it863.common

import java.util.Properties

import com.it863.common.util.JdbcUtil.urlFormat
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

object ExportHiveToMysql {

  def main(args: Array[String]): Unit = {

    val jobid = args(0)
    val timehour = args(1)

    val hdfsPath = "hdfs:///bdp/jobconfig/jobid_${jobid}.json"

    val conf = new SparkConf().setAppName("ExportHiveToMysql")
    val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()

    val row = spark.read.json("hdfs:///bdp/jobconfig/jobid_45.json").first()
    val jobConfig = row.getValuesMap(row.schema.fieldNames)

    // 获取参数
    val hiveQL = jobConfig.getOrElse("input_input_content", "")
    val saveMode = jobConfig.getOrElse("output_write_model", "overwrite")
    val jdbcUser = jobConfig.getOrElse("username", "")
    val jdbcPass = jobConfig.getOrElse("password", "")
    val jdbcHost = jobConfig.getOrElse("host", "")
    val jdbcPort = jobConfig.getOrElse("port", 3306).toInt
    val dbType = jobConfig.getOrElse("type", "").toLowerCase
    val driver = jobConfig.getOrElse("driver", null)
    val dbName = jobConfig.getOrElse("dbname", "")

    val preSql = jobConfig.getOrElse("output_pre_statment", null)
    val targetTable = jobConfig.getOrElse("output_table_name", null)

    val url = urlFormat(dbType, jdbcHost, jdbcPort, dbName)


    // 使用别名方式
    import spark.{sql => hsql}
    val hiveDF = hsql(s"${hiveQL}")

    //设置mysql连接
    val prop = new Properties()
    prop.setProperty("user", jdbcUser)
    prop.setProperty("password", jdbcPass)

    hiveDF.write.mode(SaveMode.valueOf(saveMode)).jdbc(url, targetTable, prop)

    spark.stop()
  }

}
