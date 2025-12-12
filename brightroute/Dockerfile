# Stage 1: Build with Maven and JDK 21
# Note: Using Java 21 (LTS) for Docker stability, though project targets Java 25
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy all project files
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime with JRE Alpine
# Using Java 21 (LTS) for runtime stability
FROM eclipse-temurin:21-jre-alpine

# Install wget for health checks
RUN apk add --no-cache wget

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/brightroute-0.0.1-SNAPSHOT.jar app.jar

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
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.profiles.active=docker"]
