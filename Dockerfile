#FROM eclipse-temurin:17
FROM openjdk:17-jdk-slim


ARG jarToCopy

ENV DB_USER=dbmanager \
    DB_URL=jdbc:postgresql://database:5432/hoteldb \
    DB_PASSWORD=/Pass@098/ \
	HOST_NAME=database

# Enable multi-architecture support and install required packages
RUN dpkg --add-architecture i386 && \
    apt update && \
    apt install -y --no-install-recommends \
    libxrender1 \
    libxtst6 \
	libxext6 \
    libxi6 && \
    apt clean && \
    rm -rf /var/lib/apt/lists/* 


#  COPY /target/$jarToCopy /app/app.jar

COPY /target/hotelroom-jar-with-dependencies.jar /app/app.jar

# CMD ["java", "-jar", "/app/app.jar", "hotel.Main"]