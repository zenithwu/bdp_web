package com.it863.common

import java.util.Properties

import com.it863.common.util.JdbcUtil._
import com.it863.common.util.ParseDateTime.replaceDateTime
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.slf4j.LoggerFactory

object ExportHiveToRDB {
  private val log = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val jobid = args(0)
    val timehour = args(1)

    val hdfsPath = "hdfs:///bdp/jobconfig/jobid_" + jobid + ".json"

    val conf = new SparkConf().setAppName("ExportHiveToMysql").setMaster("local[1]")
    val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
    val row = spark.read.json(hdfsPath).first()
    val jobConfig = row.getValuesMap(row.schema.fieldNames)
    // 获取参数
    val hiveQL = replaceDateTime(jobConfig.getOrElse("input_input_content", ""), timehour)
    val saveMode = jobConfig.getOrElse("output_write_model", "overwrite")
    val jdbcUser = jobConfig.getOrElse("username", "")
    val jdbcPass = jobConfig.getOrElse("password", "")
    val jdbcHost = jobConfig.getOrElse("host", "")
    val jdbcPort = jobConfig.getOrElse("port", "3306")
    val dbType = jobConfig.getOrElse("type", "").toLowerCase
    val driver = jobConfig.getOrElse("driverClass", "")
    val dbName = jobConfig.getOrElse("dbname", "")
    val preSql = replaceDateTime(jobConfig.getOrElse("output_pre_statment", ""), timehour)
    val targetTable = jobConfig.getOrElse("output_table_name", "")
    val url = urlFormat(dbType, jdbcHost, jdbcPort, dbName)

    log.info("hiveQL === " + hiveQL)
    log.info("preSql === " + preSql)

    // 使用别名方式
    import spark.{sql => hsql}
    val hiveDF = hsql(s"${hiveQL}")

    // 执行前置sql
    if (preSql != "") {
      runSQL(driver, preSql, url, jdbcUser, jdbcPass)
    }

    //设置数据库属性
    val prop = new Properties()
    prop.setProperty("user", jdbcUser)
    prop.setProperty("password", jdbcPass)
    val sm = if (saveMode == "append") SaveMode.Append else SaveMode.Overwrite

    hiveDF.write.mode(sm).jdbc(url, targetTable, prop)

    spark.stop()
  }

}
