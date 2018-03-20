package com.spark.rest

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.util.EntityUtils

import scala.util.parsing.json.{JSON, JSONObject}

/**
  *
  * Created by Ravikumar Ramasamy on 3/19/18 9:41 PM.
  */
object HttpReqUtil {

  def parseHttpResponse(response: CloseableHttpResponse) : (Boolean,JSONObject) = {
    val responseBody = EntityUtils.toString(response.getEntity)
    println(s"Response payload ${responseBody}")
    val jsonResponse = JSON.parseRaw(responseBody).get.asInstanceOf[JSONObject]
    val statusCode: Int = response.getStatusLine.getStatusCode
    if(statusCode == 200 || statusCode == 201 || statusCode == 202) {
      println(s"Successfully submitted the application ")
      (true,jsonResponse)
    } else {
      println("Error when submitting the job")
      (false,jsonResponse)
    }
  }

  def getStateValue(jsonRes: JSONObject) : (Boolean,String) = {
    val state = jsonRes.obj.get("state").get.asInstanceOf[String]

    if(state == "error" || state == "dead") {
      (false,state)
    } else {
      (true,state)
    }
  }
}
