# spark-rest-service
### Job Submission using REST API service

Data Engine platform is evolving in recent times and more business interactive platforms are coming up in the market. Customers want to run ad-hoc queries or spark jobs to pull reports from the data warehouse platform. The more interactive way of building ad-hoc job execution platform should supports submitting jobs through REST service and get the status of the jobs as well. Livy service providing this functionality.

---

This project demonstrate how to submit the Spark Jobs through REST call.

- Built the spark-analytics spark projects and create the assembly.

       Assembly jar is saved into HDFS /user/hadoop/parking/stream-analytics.jar

- Run the application with following parameters

>  **scala -e 'JobSubmitService' className jarHDFSPath [extraargs]**

For example the main class of stream-analytics application is com.ravi.spark.analytics.SparkLivyTest

    scala -e 'JobSubmitService' com.ravi.spark.analytics.SparkLivyTest /user/hadoop/parking/stream-analytics.jar
