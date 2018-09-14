package com.it863.common.util

object JdbcUtil {
  def runPreSQL(dbType: String, preSQL: String): Int = {
    return 0
  }

  def urlFormat(dbType: String, jdbcHost: String, jdbcPort: Int, dbName: String): String = {
    val url = dbType match {
      case "mysql" => s"jdbc:mysql://${jdbcHost}:${jdbcPort}/${dbName}"
      case "oracle" => s"jdbc:oracle:thin:@//${jdbcHost}:${jdbcPort}/${dbName}"
      case "mssql" => s"jdbc:microsoft:sqlserver://${jdbcHost}:${jdbcPort};DatabaseName=${dbName}"
      case "db2" => s"jdbc:db2://${jdbcHost}:${jdbcPort}/${dbName}"
    }
    return url
  }

  def runSQL(driver: String, sql: String) = {

  }


}
