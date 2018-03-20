package com.spark.rest

import com.spark.rest.JobStatusService.clusterConfig
import com.spark.rest.config.ClusterConfig
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpPost, HttpPut}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.http.util.EntityUtils

import scala.util.parsing.json.{JSON, JSONObject}


/**
  *
  * Created by Ravikumar Ramasamy on 1/29/18 10:42 PM.
  */
object JobSubmitService {

  val path = this.getClass.getResource("/yarn.conf").getPath
  val clusterConfig = ClusterConfig(path)

  /**
    * Going to implement submit Spark jobs through Livy Services.
    *
    * @param args
    */
  def main(args: Array[String]): Unit = {
    if(args.length < 2) {
      println(" Usage : JobSubmitService className jarHDFSPath [args]")
      System.exit(1)
    }
    val (className, jarHDFSPath) = (args(0), args(1))
//    val (className, jarHDFSPath) = ("com.ravi.spark.analytics.SparkLivyTest",  "/user/hadoop/parking/stream-analytics.jar")
    val extraArgs =  if(args.length > 2) { args.slice(2,args.length).toList} else List()

    val resultJson = submitJob(className, jarHDFSPath, extraArgs)

    val sessionid = resultJson.obj.get("id").get.asInstanceOf[Double].toInt

    val stateResult = HttpReqUtil.getStateValue(resultJson)

    if(stateResult._1) {
      if(stateResult._2 == "not_started") {
         Thread.sleep(2000)
      }
      val batchStatus = getApplicationBatchStatus(sessionid.toString)
      println("Job Submitted Successfully. batchStatus : "+batchStatus.toString())
    } else {
      println(" application submission failed. "+ resultJson.toString())
    }

  }

  def getApplicationBatchStatus(sessionId: String) : JSONObject = {
    val appStatusGet = new HttpGet(s"${clusterConfig.livyserver}/batches/$sessionId")
    val client: CloseableHttpClient = HttpClientBuilder.create().build()
    val response: CloseableHttpResponse = client.execute(appStatusGet)
    HttpReqUtil.parseHttpResponse(response)._2
  }

  def submitJob(className: String, jarPath:String, extraArgs: List[String]) : JSONObject = {

    val jobSubmitRequest = new HttpPost(s"${clusterConfig.livyserver}/batches")

    val data =  Map(
      "className"-> className,
      "file" -> jarPath,
      "driverMemory" -> "2g",
      "name" -> "LivyTest",
      "proxyUser" -> "hadoop")

    if(extraArgs != null && !extraArgs.isEmpty) {
      data  + ( "args" -> extraArgs)
    }

    val json = new JSONObject(data)

    val params = new StringEntity(json.toString(),"UTF-8")
    params.setContentType("application/json")

    jobSubmitRequest.addHeader("Content-Type", "application/json")
    jobSubmitRequest.addHeader("Accept", "*/*")
    jobSubmitRequest.setEntity(params)

    val client: CloseableHttpClient = HttpClientBuilder.create().build()
    val response: CloseableHttpResponse = client.execute(jobSubmitRequest)
    HttpReqUtil.parseHttpResponse(response)._2
  }


}
