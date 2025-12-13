# Docker Setup for BrightRoute

## Java 25 Compatibility Status: **WORKING** ✅

This Docker setup successfully uses **Java 25** with a workaround for the SSL/TLS certificate issues.

**Solution**: The Dockerfile downloads Maven Central's certificate directly and imports it into Java 25's truststore, allowing Maven to successfully download dependencies.

While Java 25 is **NOT** a Long-Term Support (LTS) release and is still experimental, the Docker build now works correctly. For production use, consider using Java 21 (LTS) or Java 17 (LTS) instead.

---

### Java 25 Images Used:
- **Build Stage**: `maven:3.9-eclipse-temurin-25`
- **Runtime Stage**: `eclipse-temurin:25-jdk`

## Architecture

The BrightRoute application runs in a containerized environment with the following components:

1. **brightroute-app**: Spring Boot application (Port 8080)
2. **sqlserver**: Microsoft SQL Server 2022 Express (Port 1433)
3. **sqlserver-init**: One-time database initialization container

### Database Schema

The application uses a SQL Server database named `SCM` with the following schemas:
- **users**: User and Student tables
- **courses**: Course, Lecture, and CourseSubscription tables
- **lectures**: LecturePart table
- **quiz**: Quiz, QuizQuestion, QuestionsChoice, StudentQuizSubmission, and StudentQuestionsAnswer tables
- **access**: AccessCode and Enrollment tables
- **logs**: SystemLog table

## Prerequisites

- Docker Desktop or Docker Engine (20.10+)
- Docker Compose (v2.0+)
- At least 4GB of available RAM
- Ports 8080 and 1433 available on your host machine

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd BrightRoute/brightroute
```

### 2. Build and Start the Containers

```bash
docker-compose up --build
```

This command will:
1. Pull the required images
2. Build the Spring Boot application using Java 25
3. Start the SQL Server container
4. Initialize the database schema
5. Start the BrightRoute application

### 3. Access the Application

- **Application**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **SQL Server**: localhost:1433 (sa / YourStrong@Passw0rd)

## Docker Commands

### Start the Application

```bash
docker-compose up -d
```

### Stop the Application

```bash
docker-compose down
```

### Stop and Remove All Data (including database)

```bash
docker-compose down -v
```

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f brightroute-app
docker-compose logs -f sqlserver
```

### Rebuild the Application

```bash
docker-compose up --build -d
```

### Check Container Status

```bash
docker-compose ps
```

## Configuration

### Environment Variables

The application uses the `docker` Spring profile, which loads configuration from `application-docker.properties`.

Key configuration parameters:
- **Database URL**: `jdbc:sqlserver://sqlserver:1433;databaseName=SCM`
- **Database User**: `sa`
- **Database Password**: `YourStrong@Passw0rd`
- **Server Port**: `8080`

### Database Connection

To connect to the SQL Server instance from your host machine:

```bash
# Using sqlcmd (if installed)
sqlcmd -S localhost,1433 -U sa -P "YourStrong@Passw0rd"

# Using Docker exec
docker exec -it brightroute-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "YourStrong@Passw0rd"
```

## Troubleshooting

### Java 25 Certificate Issue - RESOLVED ✅

**Issue**: Java 25 has SSL/TLS certificate validation changes that initially prevented Maven from downloading dependencies from Maven Central.

**Solution Implemented**: The Dockerfile now downloads Maven Central's certificate directly using `openssl s_client` and imports it into Java 25's truststore. This allows Maven to successfully validate the certificate chain and download dependencies.

#### How the Fix Works:

1. Downloads the certificate from `repo.maven.apache.org:443`
2. Converts it to PEM format
3. Imports it into Java's truststore at `$JAVA_HOME/lib/security/cacerts`
4. Maven can now successfully connect to Maven Central over HTTPS

This workaround is specific to Java 25's certificate handling and may not be necessary in future Java releases.

### Common Issues

#### Container won't start

```bash
# Check container logs
docker-compose logs brightroute-app

# Check if SQL Server is healthy
docker-compose ps
```

#### Database connection errors

1. Ensure SQL Server container is healthy:
   ```bash
   docker-compose ps sqlserver
   ```

2. Check SQL Server logs:
   ```bash
   docker-compose logs sqlserver
   ```

3. Verify database initialization:
   ```bash
   docker-compose logs sqlserver-init
   ```

#### Port conflicts

If ports 8080 or 1433 are already in use:

1. Stop services using those ports
2. Or modify the port mappings in `docker-compose.yml`:
   ```yaml
   ports:
     - "8081:8080"  # Change host port to 8081
   ```

#### Out of memory errors

Increase memory allocation in `docker-compose.yml`:

```yaml
environment:
  - JAVA_OPTS=-Xmx1024m -Xms512m  # Increase heap size
```

### Database Persistence

Database data is stored in a Docker volume named `sqlserver_data`. To reset the database:

```bash
# Stop containers and remove volumes
docker-compose down -v

# Start fresh
docker-compose up --build
```

## Health Checks

The application includes health checks for monitoring:

- **Application Health**: http://localhost:8080/actuator/health
- **Database Health**: Included in actuator health endpoint

Docker health checks run automatically:
- **SQL Server**: Every 10 seconds
- **Application**: Every 30 seconds

## Security Notes

⚠️ **Important Security Warnings**:

1. **Default Passwords**: Change the default SA password in production
2. **Non-root User**: The application runs as a non-root user for security
3. **Network Isolation**: Containers communicate via a dedicated bridge network

### Recommended for Production:

- Use environment variables for secrets
- Enable SSL/TLS for SQL Server
- Use stronger passwords
- Configure proper authentication and authorization
- Review and harden security settings

## Development

### Live Reload (Development Mode)

For development with live reload, mount the source code:

```yaml
volumes:
  - ./src:/app/src
```

Then rebuild the container when changes are made.

### Debugging

To enable Java debugging, add to `docker-compose.yml`:

```yaml
environment:
  - JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
ports:
  - "5005:5005"
```

## Performance Tuning

### JVM Options

Adjust in `docker-compose.yml`:

```yaml
environment:
  - JAVA_OPTS=-Xmx1g -Xms512m -XX:+UseG1GC
```

### Database Connection Pool

Settings are configured in `application-docker.properties`:
- Maximum pool size: 10
- Minimum idle connections: 5

## License

[Your License Here]

## Support

For issues and questions:
- Open an issue on GitHub
- Check the logs: `docker-compose logs`

---

**Remember**: This setup uses experimental Java 25. For production use, consider using a Long-Term Support (LTS) Java version such as Java 21 or Java 17.
