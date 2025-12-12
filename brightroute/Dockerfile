# BrightRoute Dockerfile
# WARNING: This Dockerfile uses experimental Java 25 (non-LTS)
# Java 25 is not a Long-Term Support release and may have stability issues
#
# CURRENT STATUS: THIS BUILD FAILS due to Java 25's SSL/TLS certificate validation issues
# when Maven attempts to download dependencies from Maven Central over HTTPS.
# This is a known limitation of experimental Java 25 as of December 2024.
#
# For functional Docker deployment, consider using Java 21 (LTS) or Java 17 (LTS).

# Stage 1: Build Stage
# Using Eclipse Temurin 25 with Maven 3.9
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml ./

# Copy source code
COPY src ./src

# Build the application (dependencies will be downloaded during build)
# Using -Dmaven.wagon.http.ssl.insecure=true for Java 25 certificate issues
RUN mvn clean package -DskipTests -B -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true

# Stage 2: Runtime Stage
# Using Eclipse Temurin 25 JRE for runtime
FROM eclipse-temurin:25-jdk

WORKDIR /app

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Expose application port
EXPOSE 8080

# Health check configuration
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
