FROM ubuntu:22.04

ARG jarToCopy

RUN apt update && apt install -y openjdk-17-jdk x11-apps xvfb libxrender1 libxtst6 libxext6 libxi6 && \
    apt clean && rm -rf /var/lib/apt/lists/*

RUN Xvfb :0 -screen 0 1024x768x24 &
ENV DISPLAY=:0
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

COPY /target/hotelroom-jar-with-dependencies.jar /app/app.jar