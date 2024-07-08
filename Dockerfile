# Build stage
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app

# Copy Maven configuration files
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source files and build the application
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create upload directory
RUN mkdir uploads

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8081
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "app.jar"]