# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

EXPOSE 8080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]