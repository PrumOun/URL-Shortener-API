# URL Shortener API

A production-ready backend service for creating, redirecting, and tracking analytics for shortened URLs — built with Spring Boot, following a layered architecture with proper separation of concerns, concurrency-safe click tracking, and comprehensive test coverage.

## Overview

This project is a RESTful URL shortening service, similar in concept to Bitly or TinyURL. Beyond the core "shorten a URL and redirect" functionality, it demonstrates production-grade backend engineering practices: layered architecture, soft deletion for auditability, optimistic locking and atomic database operations to handle concurrent traffic correctly, pagination, structured logging, and full API documentation via OpenAPI/Swagger.

It was built incrementally over a multi-day sprint, moving from a basic CRUD implementation toward a project that reflects real-world production concerns — concurrency safety, observability, configuration management, and containerized deployment.

## Features

- **Create Short URL** — submit a long URL and receive a unique, randomly generated short code
- **Redirect** — visiting a short link redirects (HTTP 302) to the original URL and increments its click count
- **Analytics** — view click count, creation/update timestamps, expiration, and status for any short URL
- **Top URLs** — retrieve the most-clicked URLs, ranked and limited
- **Pagination & Sorting** — list all URLs with configurable page size and sort field, with sane upper limits enforced
- **Validation** — request bodies are validated (non-blank, well-formed URLs) before reaching business logic
- **Soft Delete** — URLs are never hard-deleted; a `deleted` flag preserves records for auditing and historical analytics while blocking future redirects
- **Expiration** — URLs automatically expire 30 days after creation and stop redirecting once expired
- **Optimistic Locking & Atomic Updates** — click counting is safe under concurrent access; a dedicated atomic `UPDATE` query eliminates race conditions on the click counter without sacrificing throughput
- **Consistent Error Handling** — a centralized global exception handler returns a uniform error shape across the entire API, with no internal stack traces ever exposed to clients
- **Structured Logging** — key business events (creation, deletion, redirects, unexpected errors) are logged via SLF4J to both console and rotating log files
- **API Documentation** — interactive OpenAPI/Swagger docs generated directly from the codebase

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Language / runtime |
| Spring Boot | Application framework |
| Spring Data JPA / Hibernate | ORM and database access |
| MySQL 8 | Primary relational database |
| H2 | In-memory database for integration tests |
| Maven | Build and dependency management |
| Lombok | Boilerplate reduction (getters/setters/constructors) |
| springdoc-openapi | Swagger UI / OpenAPI documentation |
| JUnit 5 + Mockito | Unit and integration testing |
| Docker & Docker Compose | Containerization |

## Architecture

The application follows a strict layered architecture, with each layer having a single, well-defined responsibility:

```
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

- **Controller** — receives HTTP requests, delegates to the Service layer, returns HTTP responses. Contains no business logic.
- **Service** — owns all business rules (uniqueness checks, soft-delete enforcement, expiration checks, DTO mapping). Split by responsibility into `UrlService` (CRUD lifecycle), `RedirectService` (the high-traffic redirect path), and `AnalyticsService` (reporting).
- **Repository** — Spring Data JPA interfaces responsible purely for data access.
- **Database** — MySQL in production/development, H2 in-memory for automated tests.

Requests and responses never expose JPA entities directly — dedicated Request and Response DTOs decouple the API contract from the internal database schema.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/urls` | Create a short URL |
| GET | `/r/{code}` | Redirect to the original URL |
| GET | `/api/urls/{id}` | Get URL details by ID |
| GET | `/api/urls` | List URLs (paginated, sortable) |
| DELETE | `/api/urls/{id}` | Soft delete a URL |
| GET | `/api/analytics/{code}/stats` | Get analytics for a URL |
| GET | `/api/analytics/top` | Get the most-clicked URLs |

Full interactive documentation is available via Swagger UI once the application is running, at:
```
http://localhost:8080/swagger-ui.html
```

## Database Schema

**`urls` table**

| Column | Type | Constraints |
|---|---|---|
| `id` | BIGINT | Primary key, auto-increment |
| `original_url` | VARCHAR(2048) | Not null |
| `short_code` | VARCHAR(20) | Not null, unique (indexed) |
| `click_count` | BIGINT | Not null, default 0 |
| `created_at` | DATETIME | Not null, immutable |
| `updated_at` | DATETIME | Auto-updated on every change |
| `expires_at` | DATETIME | Set at creation, 30 days from `created_at` |
| `deleted` | BOOLEAN | Not null, default false (soft-delete flag) |
| `version` | BIGINT | Not null, optimistic locking token |

```
┌─────────────────────────┐
│           urls           │
├─────────────────────────┤
│ id            (PK)       │
│ original_url              │
│ short_code    (UNIQUE)    │
│ click_count                │
│ created_at                 │
│ updated_at                 │
│ expires_at                 │
│ deleted                    │
│ version                    │
└─────────────────────────┘
```

## Running the Project

### Option A — Docker Compose (recommended, no local setup required)

```bash
git clone <repository-url>
cd url-shortener
docker compose up --build
```

This builds the application image, starts a MySQL container, waits for the database to become healthy, and starts the application — fully wired together with no manual configuration.

The API will be available at `http://localhost:8080`.

### Option B — Run locally (requires Java 21, Maven, and MySQL)

**1. Clone the repository**
```bash
git clone https://github.com/PrumOun/URL-Shortener-API.git
cd url-shortener
```

**2. Configure the database**

Start a local MySQL instance (or use Docker for just the database):
```bash
docker run --name url-shortener-mysql \
  -e MYSQL_ROOT_PASSWORD=rootpass \
  -e MYSQL_DATABASE=urlshortener \
  -e MYSQL_USER=appuser \
  -e MYSQL_PASSWORD=apppass \
  -p 3307:3306 \
  -d mysql:8.0
```

Connection settings are read from `application-dev.properties`, with environment variable overrides available (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`). Defaults match the command above.

**3. Run the application**
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

### Running Tests

```bash
mvn test
```

This runs both unit tests (Service layer, mocked dependencies via Mockito) and integration tests (full request pipeline via MockMvc against an in-memory H2 database) — no external database required for testing.

## Future Improvements

- **Redis caching** for high-traffic short codes, to reduce database load on the redirect hot path
- **JWT authentication** and **user accounts**, so URLs can be owned, managed, and scoped per user
- **QR code generation** for each shortened URL
- **Click history table** — record individual click events (timestamp, referrer, rough location) rather than just an aggregate counter, enabling richer analytics
- **Rate limiting** to prevent abuse of the shortening and redirect endpoints
- **Kubernetes deployment** manifests for horizontal scaling in a production environment
