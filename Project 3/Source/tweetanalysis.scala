package com.part2.edu

import java.lang.System.setProperty
import java.lang.System._

import scala.collection.JavaConversions._
import scala.collection.convert.wrapAll._
import scala.collection.convert.decorateAll._
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * Created by user on 4/30/2017.
  */
object tweetanalysis {

  def main(args: Array[String]) {

    //println("WELCOME")
    setProperty("hadoop.home.dir", "c:\\winutils\\")

    val conf = new SparkConf().setAppName("SparkSQL").setMaster("local").set("com.spark.executor", "")

    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    // loading the tweetfile

    val jsonFile = sqlContext.jsonFile("src/main/resources/Final.json")

    jsonFile.registerTempTable("MainTable")

    val lang_refiner = sqlContext.sql("SELECT * FROM MainTable where lang='en'")

    lang_refiner.registerTempTable("Lang_Refiner")


    //retreving hashtags text from Lang_Refiner table

    val table2 = sqlContext.sql("SELECT entities.hashtags.text AS ht FROM MainTable WHERE entities.hashtags.text IS NOT NULL")

    table2.registerTempTable("Table2")


    //getting data from Table2 to split file in to words

    val table21 = sqlContext.sql("SELECT ht FROM Table2").map(l => l.getList(0))

    //converting to single array

    val htd = table21.filter(l => l.size() > 0).flatMap(tags => {
      tags.toArray
    })

    //saving the results

    htd.saveAsTextFile("HashOut2")

    // retreving the saved result file

    val htd1 = sc.textFile("HashOut2")

    val schemaString = "HashTags"

    //Creating the schema

    val schema =
      StructType(
        schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true)))

    //Converting RDD into row format to create table

    val rowRDD = htd1.map(r => r.split(",")).map(l => Row(l(0)))

    //Creating data frame with RDD and created schema

    val hashTagDataFrame = sqlContext.createDataFrame(rowRDD, schema)

    // Creating table from data frame created

    hashTagDataFrame.registerTempTable("Table21")


    val NUMTWEETS = sqlContext.sql("SELECT user.screen_name AS u_sn,count(*) AS u_count FROM Lang_Refiner GROUP BY user.screen_name ORDER BY u_count DESC LIMIT 10")

    //NUMTWEETS.map(x=> (x(0),x(1))).coalesce(1,true).saveAsTextFile("src/main/resources/Testing4")

    NUMTWEETS.registerTempTable("NT")

    //getting the users with highest followers

    val FOLLOWERC=sqlContext.sql("SELECT user.followers_count AS ft,user.screen_name AS ust,count(*) AS f_count FROM Lang_Refiner GROUP BY user.followers_count,user.screen_name ORDER BY f_count DESC LIMIT 10")

    //FOLLOWERC.map(x=> (x(0),x(1))).coalesce(1,true).saveAsTextFile("src/main/resources/Testing5")

    FOLLOWERC.registerTempTable("FC")

    //joining the two tables to get popular users with highest followers count

    val join= sqlContext.sql("SELECT NT.u_sn AS N, FC.ft FROM NT " +
      "JOIN FC ON (NT.u_sn = FC.ust) GROUP BY " +
      "NT.u_sn,FC.ft ORDER BY FC.ft DESC")
    //join.show()
    join.map(x=> (x(0),x(1))).coalesce(1,true).saveAsTextFile("src/main/resources/MostPopUsers")

  }

}
