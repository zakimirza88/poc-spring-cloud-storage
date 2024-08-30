Sample Spring Boot app to demonstrate usage of storage APIs from different cloud providers.

It uses the following Spring Cloud dependencies to simplify using AWS and OCI APIs and services 
in a Spring Boot application.

<!-- Spring Cloud AWS -->
<dependency>
<groupId>io.awspring.cloud</groupId>
<artifactId>spring-cloud-aws-starter-s3</artifactId>
</dependency>
 
<!-- Spring Cloud OCI -->
<dependency>
<groupId>com.oracle.cloud.spring</groupId>
<artifactId>spring-cloud-oci-starter-storage</artifactId>
</dependency>


References:
https://github.com/oracle/spring-cloud-oracle
https://github.com/awspring/spring-cloud-aws
