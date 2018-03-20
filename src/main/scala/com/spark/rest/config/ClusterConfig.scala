package com.spark.rest.config

import java.io.File

import com.typesafe.config.ConfigFactory

/**
  *
  * Created by Ravikumar Ramasamy on 1/31/18 10:30 PM.
  */
class ClusterConfig(confFileName: String) {
  lazy val clusterConf = ConfigFactory.parseFile(new File(confFileName))
  lazy val resourcemanager = clusterConf.getString("resourcemanager")
  lazy val livyserver = clusterConf.getString("livyserver")
}

object ClusterConfig {
  def apply(confFileName: String): ClusterConfig = new ClusterConfig(confFileName)
}