# JDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Container Working Directory
WORKDIR /app

# Maven target folder se built JAR file copy karein
COPY target/*.jar app.jar

# Application port expose 
EXPOSE 8080

# App running entrypoint command
ENTRYPOINT ["java","-jar","app.jar"]