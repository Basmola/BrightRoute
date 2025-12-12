# BrightRoute Dockerfile
# Java 25 Build with Certificate Workaround
# 
# Java 25 has SSL/TLS certificate validation issues that prevent Maven from downloading
# dependencies from Maven Central. This Dockerfile works around the issue by downloading
# Maven Central's certificate and importing it into Java's truststore.

# Stage 1: Build Stage
# Using Eclipse Temurin 25 with Maven 3.9
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

# Workaround for Java 25 certificate issues:
# Download Maven Central's certificate and import it into Java's truststore
RUN apt-get update && apt-get install -y openssl && \
    # Download Maven Central's certificate chain
    echo | openssl s_client -servername repo.maven.apache.org -connect repo.maven.apache.org:443 2>/dev/null | \
    openssl x509 -outform PEM > /tmp/maven-central.pem && \
    # Import into Java truststore
    keytool -import -trustcacerts -noprompt -alias maven-central \
      -file /tmp/maven-central.pem \
      -keystore $JAVA_HOME/lib/security/cacerts \
      -storepass changeit && \
    rm -rf /var/lib/apt/lists/* /tmp/maven-central.pem

# Copy pom.xml first for dependency caching
COPY pom.xml ./

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

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
