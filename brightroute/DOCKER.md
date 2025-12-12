# Docker Setup for BrightRoute

This document explains how to run the BrightRoute application using Docker.

## Prerequisites

- Docker Engine (version 20.10 or higher)
- Docker Compose (version 2.0 or higher)
- At least 4GB of available RAM
- At least 10GB of available disk space

## Quick Start

### Option 1: Using Multi-Stage Build (Recommended)

If you have a clean internet connection and proper SSL certificates:

```bash
cd brightroute
docker compose up -d
```

### Option 2: Using Pre-Built JAR

If you encounter network or SSL issues during Docker build:

1. Build the JAR locally first:
```bash
cd brightroute
mvn clean package -DskipTests
```

2. Update docker-compose.yml to use Dockerfile.simple:
```yaml
brightroute-app:
  build:
    context: .
    dockerfile: Dockerfile.simple  # Change from Dockerfile
```

3. Start the services:
```bash
docker compose up -d
```

## Architecture

The Docker setup consists of three services:

### 1. sqlserver
- **Image**: Microsoft SQL Server 2022 Express Edition
- **Port**: 1433
- **Credentials**: 
  - Username: `sa`
  - Password: `YourStrong@Passw0rd`
- **Volume**: `sqlserver_data` for data persistence
- **Health Check**: Ensures database is ready before other services start

### 2. sqlserver-init
- **Purpose**: One-time database initialization
- **Runs**: Database schema creation script (`init-db/init.sql`)
- **Creates**: 
  - Database: `SCM`
  - Schemas: `users`, `courses`, `lectures`, `quiz`, `access`, `logs`
  - All 14 tables with proper relationships and indexes
- **Lifecycle**: Runs once and exits after completion

### 3. brightroute-app
- **Image**: Spring Boot application with JRE 17 Alpine
- **Port**: 8080
- **Profile**: `docker` (uses `application-docker.properties`)
- **Health Check**: Monitors `/actuator/health` endpoint
- **Dependencies**: Waits for database health check and initialization

## Access Points

Once all services are running:

- **Application API**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **SQL Server**: localhost:1433

## Useful Commands

### View logs
```bash
# All services
docker compose logs -f

# Specific service
docker compose logs -f brightroute-app
docker compose logs -f sqlserver
```

### Check service status
```bash
docker compose ps
```

### Stop services
```bash
docker compose down
```

### Stop services and remove volumes (⚠️ deletes all data)
```bash
docker compose down -v
```

### Rebuild application
```bash
# Rebuild and restart
docker compose up -d --build brightroute-app
```

### Access SQL Server CLI
```bash
docker exec -it brightroute-sqlserver /opt/mssql-tools18/bin/sqlcmd \
  -S localhost -U sa -P YourStrong@Passw0rd -C
```

## Troubleshooting

### Database connection issues
1. Check SQL Server health:
```bash
docker compose ps sqlserver
```

2. View SQL Server logs:
```bash
docker compose logs sqlserver
```

3. Ensure initialization completed:
```bash
docker compose logs sqlserver-init
```

### Application startup issues
1. Check application logs:
```bash
docker compose logs brightroute-app
```

2. Verify database is accessible:
```bash
docker exec brightroute-app wget -O- http://localhost:8080/actuator/health
```

### Build failures
If the multi-stage build fails due to network issues:
- Use Option 2 (Pre-Built JAR) from Quick Start
- The Dockerfile.simple uses a pre-built JAR and avoids network issues

## File Structure

```
brightroute/
├── Dockerfile              # Multi-stage build (recommended)
├── Dockerfile.simple       # Single-stage build (pre-built JAR)
├── docker-compose.yml      # Service orchestration
├── .dockerignore          # Files to exclude from build
├── init-db/
│   └── init.sql           # Database schema initialization
└── src/main/resources/
    └── application-docker.properties  # Docker environment config
```

## Security Notes

- The application runs as a non-root user (`spring:spring`) for security
- Default SQL Server password should be changed for production use
- SSL is disabled for local development (set `encrypt=true` for production)
- Health checks are configured for all services

## Production Considerations

For production deployment:
1. Change SQL Server password in both `docker-compose.yml` and `application-docker.properties`
2. Enable SSL/TLS encryption for database connections
3. Use Docker secrets for sensitive credentials
4. Configure proper resource limits for containers
5. Set up backup strategy for the `sqlserver_data` volume
6. Use a reverse proxy (nginx/traefik) for the application
7. Configure proper logging and monitoring
