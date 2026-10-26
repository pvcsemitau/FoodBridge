
# Stage 1: Build the application using Maven and Java 25
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first for efficient caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the executable jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application using Java 25 JRE
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the web application
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
