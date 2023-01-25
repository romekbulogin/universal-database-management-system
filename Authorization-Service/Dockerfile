FROM openjdk:17
LABEL version="1.0"
LABEL description="First image with Dockerfile."
ADD /target/authorization-service-0.0.1.jar authorization-service.jar
ENTRYPOINT ["java","-jar","authorization-service.jar"]