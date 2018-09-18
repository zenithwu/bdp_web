package com.it863.common.util

import java.sql.{Connection, DriverManager, Statement}

import com.it863.common.util.ParseDateTime.replaceDateTime

object JdbcUtil {
  def runPreSQL(dbType: String, preSQL: String): Int = {
    return 0
  }

  def urlFormat(dbType: String, jdbcHost: String, jdbcPort: String, dbName: String): String = {
    dbType match {
      case "mysql" => s"jdbc:mysql://${jdbcHost}:${jdbcPort}/${dbName}"
      case "oracle" => s"jdbc:oracle:thin:@//${jdbcHost}:${jdbcPort}/${dbName}"
      case "mssql" => s"jdbc:microsoft:sqlserver://${jdbcHost}:${jdbcPort};DatabaseName=${dbName}"
      case "db2" => s"jdbc:db2://${jdbcHost}:${jdbcPort}/${dbName}"
    }
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

}
