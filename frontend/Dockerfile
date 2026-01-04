FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
# Use docker profile
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]
