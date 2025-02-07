FROM openjdk:17-jdk-alpine

MAINTAINER chlok

COPY target/bookmanagementsystem-0.0.1-SNAPSHOT.jar bookmanagementsystem-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/bookmanagementsystem-0.0.1-SNAPSHOT.jar"]