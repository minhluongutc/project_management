# Use a Maven image to build the application
FROM maven:3.8.8-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .

# Copy the source code into the container
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use an OpenJDK runtime image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
