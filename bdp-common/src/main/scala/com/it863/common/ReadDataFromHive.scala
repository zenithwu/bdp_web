package com.it863.common


import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession;


object ReadDataFromHive {
  def main(args: Array[String]): Unit = {

    if (args.length < 1) {
      println("Hive Table Name is missing")
      println("It should be:")
      println(ReadDataFromHive.getClass.getSimpleName() + " HiveTableName")
      System.exit(1)
    }

    val HiveTableName = args(0)

    val conf = new SparkConf().setAppName("ReadDataFromHive")
    val spark = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
    val hiveTableDF = spark.sql(s"select * from ${HiveTableName}")

    hiveTableDF.show()
    hiveTableDF.collect().foreach(x => println("line" + x))

    spark.stop()
  }

}
