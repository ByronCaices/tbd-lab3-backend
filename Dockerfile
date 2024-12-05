#FROM ubuntu:latest
FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ecommerce-backend.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar","ecommerce-backend.jar"]