FROM openjdk:17
FROM rabbitmq
ADD ./target/query-executor-0.0.1.jar query-executor.jar
ENTRYPOINT ["java","-jar","query-executor.jar"]
