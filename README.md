# spark-rest-service
### Job Submission using REST API service

Customers want to run ad-hoc queries or spark jobs to pull reports from the data warehouse platform. So Data platform requires ad-hoc query execution engine to meet customer demands on the fly.  Submitting jobs via REST service is not available out of box from Hadoop/Spark stack. Apache Livy service ( Still incubating projects) provides basic functionalities of the REST requirements. There is no need of any third party libraries, just submit Spark jobs through Http REST call.

---

This project demonstrate how to submit the Spark Jobs through REST call.

- Built the spark-analytics spark projects and create the assembly.

       spark analyics assembly jar is saved into HDFS /user/hadoop/parking/stream-analytics.jar

- Run the application with following parameters

>  **scala -e 'JobSubmitService' className jarHDFSPath [extraargs]**

For example the main class of stream-analytics application is com.ravi.spark.analytics.SparkLivyTest

    scala -e 'JobSubmitService' com.ravi.spark.analytics.SparkLivyTest /user/hadoop/parking/stream-analytics.jar
