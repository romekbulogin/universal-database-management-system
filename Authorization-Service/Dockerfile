FROM openjdk:17
EXPOSE 8080
EXPOSE 587
ADD ./Authorization-Service/target/authorization-service-0.0.1.jar authorization-service.jar
CMD ["java","-jar","authorization-service.jar"]
