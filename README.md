# 💰 Finance Tracker API

Personal Finance Tracker REST API built with **Spring Boot 4**, **Java 21**, and **Hexagonal Architecture**.

Manage your income, expenses, categories, and budgets with a clean, well-documented API.

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.4-brightgreen?style=flat-square)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

## 🏗️ Architecture

This project follows **Hexagonal Architecture (Ports & Adapters)** to ensure clean separation of concerns:

```
┌──────────────────────────────────────────────────┐
│              INFRASTRUCTURE LAYER                 │
│  ┌────────────────┐     ┌─────────────────────┐  │
│  │ REST Controllers│     │  JPA Repositories   │  │
│  │ (Driving       │     │  (Driven Adapters)   │  │
│  │  Adapters)     │     │                      │  │
│  └───────┬────────┘     └──────────▲───────────┘  │
│          │                         │              │
│    ┌─────▼─────────────────────────┴────────┐     │
│    │          APPLICATION LAYER             │     │
│    │   (Use Case Services / Orchestration)  │     │
│    └────────────────┬───────────────────────┘     │
│                     │                             │
│    ┌────────────────▼───────────────────────┐     │
│    │           DOMAIN LAYER                 │     │
│    │  Entities • Value Objects • Ports      │     │
│    │  ⚠️ Zero framework dependencies!      │     │
│    └────────────────────────────────────────┘     │
└──────────────────────────────────────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven 3.9+

### Run with Docker Compose
```bash
# Clone the repository
git clone https://github.com/yourusername/finance-tracker-api.git
cd finance-tracker-api

# Start PostgreSQL
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080/api`

### Swagger UI
Open `http://localhost:8080/api/swagger-ui.html` for interactive API documentation.

## 🧪 Testing

```bash
# Run all tests
./mvnw verify

# Run unit tests only
./mvnw test

# Run with verbose output
./mvnw test -Dtest="*Test" -Dsurefire.useFile=false
```

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language (LTS) |
| Spring Boot 4.0.4 | Application framework |
| Spring Security | Authentication & authorization |
| Spring Data JPA | Data access layer |
| PostgreSQL 16 | Primary database |
| Flyway | Database migrations |
| SpringDoc OpenAPI | API documentation (Swagger UI) |
| Lombok | Boilerplate reduction |
| JUnit 5 + Mockito | Unit testing |
| Testcontainers | Integration testing |
| Docker | Containerization |

## 📁 Project Structure

```
src/main/java/com/financetracker/
├── domain/                    # Core business logic (no dependencies)
│   ├── model/                 # Entities and Value Objects
│   ├── exception/             # Domain exceptions
│   └── port/out/              # Repository interfaces (driven ports)
├── application/               # Use case orchestration
│   └── service/               # Application services
└── infrastructure/            # Framework-specific code
    ├── adapter/
    │   ├── in/web/            # REST controllers (driving adapters)
    │   └── out/persistence/   # JPA implementations (driven adapters)
    ├── config/                # Spring configuration
    └── exception/             # Global error handling
```

## 🗺️ Roadmap

- [x] MVP: CRUD categories & transactions, Flyway migrations, Swagger UI
- [ ] V1: JWT authentication, budgets, reports, CI/CD
- [ ] V2: Email notifications, multi-currency, caching, audit log

## 📄 License

This project is licensed under the MIT License.
