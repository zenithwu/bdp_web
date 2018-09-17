package com.it863.common.util

import java.sql.{Connection, DriverManager, Statement}

object JdbcUtil {
  def runPreSQL(dbType: String, preSQL: String): Int = {
    return 0
  }

  def urlFormat(dbType: String, jdbcHost: String, jdbcPort: String, dbName: String): String = {
    val url = dbType match {
      case "mysql" => s"jdbc:mysql://${jdbcHost}:${jdbcPort}/${dbName}"
      case "oracle" => s"jdbc:oracle:thin:@//${jdbcHost}:${jdbcPort}/${dbName}"
      case "mssql" => s"jdbc:microsoft:sqlserver://${jdbcHost}:${jdbcPort};DatabaseName=${dbName}"
      case "db2" => s"jdbc:db2://${jdbcHost}:${jdbcPort}/${dbName}"
    }
    return url
  }

  def runSQL(driver: String, sql: String, url: String, user: String, password: String): Unit = {
    if (driver == "") {
      throw new RuntimeException("driver is null")
    }
    val sqlList = sql.split(";")

    var connection: Connection = null
    var statement: Statement = null

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, user, password)
      statement = connection.createStatement()
      sqlList.foreach(sql => statement.execute(sql.trim))

    } catch {
      case e => e.printStackTrace()
    } finally {
      statement.closeOnCompletion()
      connection.close()
    }
  }


  def main(args: Array[String]): Unit = {
    val driver = "com.mysql.jdbc.Driver"
    val sql = "DELETE FROM sysmenu_callback WHERE id = 105;DELETE FROM sysmenu_callback WHERE id = 106;;;;"
    val url = "jdbc:mysql://192.168.0.203:3306/bdp"
    val user = "bdp"
    val password = "bdp"

    runSQL(driver, sql, url, user, password)
  }

}
