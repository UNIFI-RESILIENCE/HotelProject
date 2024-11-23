#FROM eclipse-temurin:17
FROM openjdk:17-jdk-slim

ARG jarToCopy

COPY /target/$jarToCopy /app/app.jar

COPY /target/hotelroom-jar-with-dependencies.jar /app/app.jar

CMD ["java", "-cp", "/app/app.jar", "hotel/Main"]