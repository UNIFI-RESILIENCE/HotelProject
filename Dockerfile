FROM openjdk:17

COPY /target/hotelroom-jar-with-dependencies.jar /app/app.jar

CMD ["java", "-cp", "/app/app.jar", "com.examples.hellodocker.App"]