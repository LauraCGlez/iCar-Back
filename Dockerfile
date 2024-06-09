FROM openjdk:21-jdk

LABEL authors="lizlauracglez" maintainer="lizlauracglez"

EXPOSE 8080

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]