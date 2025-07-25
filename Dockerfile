# Use a base image with Java 17
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Optional: volume for logs or temp files
VOLUME /tmp

# Copy the JAR file from target
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port used by Spring Boot
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

