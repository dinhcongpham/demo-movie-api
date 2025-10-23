FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/demo-api-movie-0.0.1-SNAPSHOT.jar demo-api-movie-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "demo-api-movie-v1.0.jar"]