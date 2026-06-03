# PGESTION ONE 

## Current Status
> Under active development.

The project is currently focused on implementing business logic and role-based access control for project and task management.

## Overview
PGestion One is a RESTful API designed for project and task management within enterprise environments.

The system aims to streamline project organization, task assignment, and team collaboration through a role-based access model. 
It provides a centralized solution for managing projects, tracking responsibilities, and supporting organizational workflows.

## Tech Stack
#### Backend
- Java
- Spring Boot
- Spring Data JPA
- Spring Validation
- Lombok
- ModelMapper
- MapStruct
  
#### Database
- H2 Database (development)
- MariaDB (local environment)
  
#### Planned Infrastructure
- PostgreSQL (Neon)
- Docker
- CI/CD Pipeline

---

## Architecture

The application follows a layered architecture to ensure maintainability, scalability, and separation of concerns.

Controller
    ↓
Service
    ↓
Repository
    ↓
Database

#### Core layers:

Entities
Repositories
Services
DTOs
Mappers
Validations

---

## Getting Started
Clone the repository

```bash
git clone https://github.com/AliVillaDeLobos/PGestionOne.git

mvn spring-boot:run
```

---

## Roadmap
1 Authentication and authorization
2 Advanced role management
3 OpenAPI / Swagger documentation
4 PostgreSQL migration with Neon
5 Docker support
6 CI/CD integration
6 Audit logging

---

## License

This project is intended for learning, portfolio, and professional development purposes.
