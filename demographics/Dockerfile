# Java 21 runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the packaged jar
COPY target/medilabo-0.0.1-SNAPSHOT.jar app.jar

# Use docker profile
ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]
