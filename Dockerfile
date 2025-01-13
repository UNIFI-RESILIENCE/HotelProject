#FROM eclipse-temurin:17
FROM openjdk:17-jdk-slim

ARG jarToCopy
ARG   dbHost
ARG    dbName
ARG    dbPassword 

ENV DB_USER=dbmanager \
    DB_URL=jdbc:postgresql://database:5432/hoteldb \
    DB_PASSWORD=0Pass-0980 \
	HOST_NAME=database

#  COPY /target/$jarToCopy /app/app.jar

COPY /target/hotelroom-jar-with-dependencies.jar /app/app.jar

# CMD ["java", "-jar", "/app/app.jar", "hotel.Main"]