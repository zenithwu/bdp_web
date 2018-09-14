package com.it863.common

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession;


object ReadDataFromMySQL {
  def main(args: Array[String]): Unit = {

    if (args.length < 4) {
      println("MySQL Table Name is missing")
      println("It should be:")
      println(ReadDataFromHive.getClass.getSimpleName() + " MySQLTableName URL USER PASSWORD")
      System.exit(1)
    }

    val table = args(0)
    val url = args(1)
    val user = args(2)
    val pass = args(3)


    val conf = new SparkConf()
    val spark = SparkSession.builder().config(conf).getOrCreate()

    // 方式一
    val mysqldf = spark.read.format("jdbc").option("url",url).option("user",user).option("password",pass).option("dbtable",table).load()


    // 方式二
    //    val prop = new Properties()
    //    prop.put("user", user)
    //    prop.put("password", pass)
    //    val mysqldf = spark.read.jdbc(url, table, prop)


    mysqldf.collect().foreach(x => print(x))
    spark.stop()
  }

}
