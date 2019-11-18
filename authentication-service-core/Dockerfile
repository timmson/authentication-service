FROM openjdk:11-jdk
ARG jarFile

WORKDIR /

COPY ${jarFile} /app.jar
#COPY authentication-service-core/application.yml /application.yml

EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]