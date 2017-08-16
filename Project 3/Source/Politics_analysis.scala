package com.pb.edu


import java.lang.System._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.Row
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.explode


import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StructType,StructField,StringType}

object Politics_analysis {

  def main(args: Array[String]) {
    val sc = new SparkContext("local[2]","Hello")
    val sqlContext = new SQLContext(sc)
    val tweets = sqlContext.jsonFile("C:\\Users\\User\\Desktop\\Folders\\pbproject3\\Final.json")
    tweets.registerTempTable("testtweets")

    val time_refine = sqlContext.sql("SELECT * from testtweets where user.time_zone IS NOT NULL")
    tweets.registerTempTable("zone_refiner")

/*
    //location refiner for devices rdd

    val location_refiner=sqlContext.sql("SELECT * FROM testtweets where user.location IS NOT NULL")
    location_refiner.registerTempTable("Location_Refiner")
    val loct = sqlContext.sql("SELECT * FROM Location_Refiner where user.location = 'United States' ")
    loct.save("loc_queries_us", "json")
    loct.show()
    loct.registerTempTable("locafter")*/

    val textFile = sc.textFile("C:\\Users\\User\\Desktop\\Folders\\pbproject3\\trends.json")




    def index[A](f: => A) = {
      val s = System.nanoTime
      val ret = f
      println("Execution time : " + (System.nanoTime - s) / 1e9 + " sec")
      ret
    }


    val hashtags = sqlContext.read.json("C:\\Users\\User\\Desktop\\Folders\\pbproject3\\trends.json").toDF()
    val trends=hashtags.select(explode($"details.trends").as("trend"))
    trends.printSchema()
    val names=trends.select(explode($"trend.name").as("names"))
    names.printSchema()
    names.show()




/*
    // Dataframe for device query(main)

    val trendsFile = sqlContext.jsonFile("C:\\Users\\User\\Desktop\\Folders\\pbproject3\\Final.json")
    trendsFile.registerTempTable("urltrends")

    index {
      val trends_query = sqlContext.sql("select user.location,count(*) as users from urltrends where " +
        "user.location like '%,%' and user.location not like '%1%' " +
        "and source like '%Twitter for iPad%' group by user.location order by users desc limit 10")
      //trends_query.show()
      trends_query.save("tipad_file", "json")
    }*/



/*
    index{
      // Data frame for Retweet count for username and language filter (main)

      val re_tweet_query = sqlContext.sql("select user.name as name, retweeted_status.retweet_count " +
        "as cnt, user.location as location from " +
        "zone_refiner where user.name is not NULL and " +
        "user.location like '%,%' and " +
        "user.location not like '%1%' order by cnt desc limit 11")

      //re_tweet_query.show()
      re_tweet_query.save("location_ret2","json")
    }*/


      /*  index {
          // RDD for different trends analysis

          val le= (textFile.filter(line => line.contains("#LEmissionPolitique")).count())
          val ga= (textFile.filter(line => line.contains("#Gala15GHVIP5")).count())
          val ro= (textFile.filter(line => line.contains("#RockInRio")).count())
          val sc= (textFile.filter(line => line.contains("#Scandal")).count())
          val so= (textFile.filter(line => line.contains("#SignOfTheTimes")).count())
          val ye= (textFile.filter(line => line.contains("#5YearswithEXO")).count())
          val tt= (textFile.filter(line => line.contains("#TimeTravelToThe90sIn4Words")).count())
          val cg= (textFile.filter(line => line.contains("#ChineseGP")).count())
          val dt= (textFile.filter(line => line.contains("#DodgeTheArrowAteneo")).count())
          val uf= (textFile.filter(line => line.contains("#UFC210")).count())

          println(("LEmissionPolitique : %s \n Gala15GHVIP5 : %s \n " + "RockInRio : %s \n Scandal : %s \n " +
            "SignOfTheTimes : %s \n 5YearswithEXO : %s \n " +
            "TimeTravelToThe90sIn4Words : %s"+ "ChineseGP : %s \n " +
            "DodgeTheArrowAteneo : %s \n " + "UFC210 : %s \n").format(le,ga,ro,sc,so,ye,tt,cg,dt,uf))

        }*/

/*
    index {
      // RDD for different politics analysis

      val Sunday= (textFile.filter(line => line.contains("Sun")).count())
      val Monday= (textFile.filter(line => line.contains("Mon")).count())
      val Tuesday= (textFile.filter(line => line.contains("Tue")).count())
      val Wednesday= (textFile.filter(line => line.contains("Wed")).count())
      val Thursday= (textFile.filter(line => line.contains("Thu")).count())
      val Friday= (textFile.filter(line => line.contains("Fri")).count())
      val Saturday= (textFile.filter(line => line.contains("Sat")).count())

      println(("US Region: \n" + "Number of tweets posted " +
        "on Sunday : %s \n Number of tweets posted on Monday : %s \n " + "Number of tweets " +
        "posted on Tuesday : %s \n Number of tweets posted on Wednesday : %s \n " +
        "Number of tweets posted on Thursday : %s \n Number of tweets " +
        "posted on Friday : %s \n Number of tweets " +
        "posted on Saturday : %s").format(Sunday,Monday,Tuesday,
        Wednesday,Thursday,Friday,Saturday))

    }*/


    sc.stop()

  }
}