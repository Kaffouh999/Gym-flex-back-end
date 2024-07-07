# Build stage
FROM openjdk:17-jdk-slim AS build
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy Maven wrapper files
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# List directory contents to verify files
RUN ls -la

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source files and build the application
COPY src src
RUN mvn package -DskipTests

# List contents of target directory
RUN ls -la target

# Runtime stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Create upload directory
RUN mkdir -p /app/uploads

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar ./app.jar

# Make port 8081 available to the world outside this container
EXPOSE 8081

# Run the jar file
CMD ["java", "-jar", "app.jar"]