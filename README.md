# JobTracker

A full-stack job tracking application built with Spring Boot and Angular.

## Architecture

- **Backend**: Spring Boot 3.5.0 with Java 21
- **Frontend**: Angular 20 with TypeScript
- **Database**: PostgreSQL 15.3
- **Containerization**: Docker with multi-stage builds
- **Development**: Docker Compose with hot reload

## Features

- Job application tracking
- RESTful API with Spring Boot
- Modern Angular frontend
- Database persistence with JPA/Hibernate
- Development environment with hot reload
- Production-ready Docker configuration

## Prerequisites

- Docker and Docker Compose
- Git

## Quick Start

### Development Environment

1. Clone the repository:
   ```bash
   git clone https://github.com/Zac-Jones/JobTracker.git
   cd JobTracker
   ```

2. Create environment file:
   ```bash
   cp .env.example .env
   ```

3. Start development environment:
   ```bash
   docker-compose -f docker-compose.dev.yml up
   ```

4. Access the application:
   - Frontend: http://localhost:4200
   - Backend API: http://localhost:8080
   - Database: localhost:5432

### Production Environment

1. Start production environment:
   ```bash
   docker-compose up
   ```

2. Access the application:
   - Application: http://localhost (or configured port)

## Development

### Backend Development

- Java 21 with Spring Boot 3.5.0
- Maven for dependency management
- Hot reload with Spring DevTools and auto-recompile script
- Debug port: 5005

### Frontend Development

- Angular 20 with TypeScript 5.8
- Hot reload enabled
- Development server on port 4200

### Database

- PostgreSQL 15.3
- Development data is persistent in Docker volume
- JPA/Hibernate for ORM

## Environment Variables

Create a `.env` file based on `.env.example` and configure:

- Database credentials
- Application ports
- Development settings

## Docker Configuration

### Development
- Backend: Hot reload with volume mounts
- Frontend: Angular dev server with live reload
- Database: Persistent development data

### Production
- Multi-stage builds for optimized images
- Nginx for frontend static file serving
- Security headers and optimizations

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test your changes
5. Submit a pull request
