package com.spark.rest

import com.spark.rest.config.ClusterConfig
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet, HttpPut}
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.http.util.EntityUtils

import scala.util.parsing.json.{JSON, JSONObject}

/**
  *
  * Created by Ravikumar Ramasamy on 1/30/18 11:04 PM.
  */
object JobStatusService {

  val clusterConfig = ClusterConfig("yarn.conf")

 // val applicationId = "application_1514936860276_19568"

  def main(args: Array[String]): Unit = {
       if(args.length < 1) {
         println(" Usage: JobStatusService applicationId ")
         System.exit(1)
       }
       val applicationId = args(1)
       println(getApplicationStatus(applicationId))
       println(killApplication(applicationId))
  }

  def getApplicationStatus(applicationId: String) : JSONObject = {
    val appStatusGet = new HttpGet(s"${clusterConfig.resourcemanager}/ws/v1/cluster/apps/$applicationId")
    val client: CloseableHttpClient = HttpClientBuilder.create().build()
    val response: CloseableHttpResponse = client.execute(appStatusGet)
    val responseBody = EntityUtils.toString(response.getEntity)
    println(s"Response payload ${responseBody}")
    println(s"Response status: ${response.getStatusLine.getStatusCode}")
    JSON.parseRaw(responseBody).get.asInstanceOf[JSONObject]
  }

  def killApplication(applicationId: String) : Boolean = {
    val appKillPut = new HttpPut(s"${clusterConfig.resourcemanager}/ws/v1/cluster/apps/$applicationId/state")
    val json = new JSONObject(Map("state"-> "KILLED"))

    val params = new StringEntity(json.toString(),"UTF-8")
    params.setContentType("application/json")

    appKillPut.addHeader("Content-Type", "application/json")
    appKillPut.addHeader("Accept", "*/*")
    appKillPut.setEntity(params)

//    println(s"Request payload ${json.toString}")

    val client: CloseableHttpClient = HttpClientBuilder.create().build()
    val response: CloseableHttpResponse = client.execute(appKillPut)
    val responseBody = EntityUtils.toString(response.getEntity)
    println(s"Response payload ${responseBody}")
    val statusCode: Int = response.getStatusLine.getStatusCode
    if(statusCode == 200 || statusCode == 201 || statusCode == 202) {
      println(s"Successfully stopped the application : ${applicationId}")
      true
    } else {
      false
    }
  }
}
