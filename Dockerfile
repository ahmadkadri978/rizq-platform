# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory in the container
WORKDIR /app

# Add a volume for logs or temp files (optional)
VOLUME /tmp

# Copy the built JAR file into the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port Spring Boot uses
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
