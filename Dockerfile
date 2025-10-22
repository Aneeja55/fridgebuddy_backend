# Use OpenJDK base image
FROM openjdk:22-jdk-slim

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "target/fridgebuddy-1.0.0.jar"]
