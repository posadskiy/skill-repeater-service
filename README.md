# Skill Repeater Service

Micronaut backend for the Skill Repeater platform. Provides a REST API for managing skills with spaced-repetition logic, repetition history tracking, and scheduled email reminders.

## Features

- **Skill CRUD** — create, read, update, and delete skills per user
- **Spaced repetition engine** — on repeat, advances skill level and calculates next repetition date based on configurable period and interval
- **Repetition history** — persistent log of every repeat action per skill and user
- **Scheduled reminders** — background cron job detects skills due for repetition and sends email notifications via external email template service
- **OpenAPI / Swagger UI** — auto-generated API documentation
- **Distributed tracing** — Jaeger integration for request tracing
- **Metrics** — Prometheus endpoint for monitoring
- **JWT security** — all endpoints require authenticated Bearer tokens

## API Endpoints

### Skill Management (`/v0/skill`)

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/v0/skill/get/{id}` | Get a skill by ID |
| `GET` | `/v0/skill/get-all/{userId}` | Get all skills for a user |
| `POST` | `/v0/skill/add` | Create a new skill |
| `POST` | `/v0/skill/add-all` | Create multiple skills in batch |
| `POST` | `/v0/skill/edit` | Update an existing skill |
| `POST` | `/v0/skill/delete` | Delete a skill (by body) |
| `DELETE` | `/v0/skill/delete/{id}` | Delete a skill by ID |
| `DELETE` | `/v0/skill/delete-all/{userId}` | Delete all skills for a user |

### Repetition (`/v0/skill/repeat`)

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/v0/skill/repeat/{id}` | Mark a skill as repeated (advances level, updates next date, logs history) |

### Repeat History (`/v0/repeat-history`)

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/v0/repeat-history/skill/{skillId}` | Get repetition history for a skill |
| `GET` | `/v0/repeat-history/user/{userId}` | Get repetition history for all of a user's skills |

### Infrastructure

| Path | Description |
|------|-------------|
| `/health` | Health check (Micronaut management) |
| `/swagger-ui` | Interactive API documentation |
| `/prometheus` | Metrics endpoint |

## Module Structure

```
skill-repeater-service/
├── pom.xml                       # Parent POM (Micronaut 4.5, Java 25)
├── skill-repeater-api/           # Shared DTOs, enums (Priority, Period), validation
├── skill-repeater-core/          # Domain logic
│   ├── model/                    # Skill, RepeatHistory entities
│   ├── repository/               # Micronaut Data JDBC repositories
│   ├── service/                  # SkillService, RepeatHistoryService
│   ├── worker/                   # RepeatServiceImpl (scheduled reminder job)
│   └── notification/             # Email notification via external services
└── skill-repeater-web/           # HTTP application
    ├── controller/               # SkillController, RepeatController, RepeatHistoryController
    ├── exception/                # Global exception handlers
    └── resources/
        ├── application-dev.yml   # Dev config (local PostgreSQL)
        ├── application-prod.yml  # Prod config (env-based)
        └── db/migration/         # Flyway migrations (V1–V7)
```

## Tech Stack

- **Runtime:** Java 25, Micronaut 4.5 (Netty)
- **Build:** Maven (multi-module), Maven Shade (fat JAR)
- **Database:** PostgreSQL via Micronaut Data JDBC + HikariCP
- **Migrations:** Flyway (7 migration files)
- **Security:** Micronaut Security JWT (Bearer tokens)
- **API docs:** OpenAPI 3.0 + Swagger UI
- **Observability:** Jaeger tracing, Micrometer + Prometheus
- **Logging:** Logback
- **Testing:** JUnit 5, H2 (in-memory)

## Domain Model

A **Skill** has:
- `name`, `description` — what to practice
- `period` (HOURS / DAYS / WEEKS / MONTHS / YEARS) + `number` — repetition interval
- `level` — increments on each repeat
- `priority` (LOW / MEDIUM / HIGH / CRITICAL) — urgency ranking
- `lastRepeated`, `nextRepeated` — timestamps managed by the repeat engine

On **repeat**: `lastRepeated` = now, `nextRepeated` = now + (number × period), `level` += 1.

## Configuration

| Variable | Description | Default (dev) |
|----------|-------------|---------------|
| `DATASOURCE_URL` | JDBC connection string | `jdbc:postgresql://localhost:5433/skill_repeater_db` |
| `DATASOURCE_USERNAME` | Database username | — |
| `DATASOURCE_PASSWORD` | Database password | — |
| `JWT_GENERATOR_SIGNATURE_SECRET` | JWT signing secret | — |
| `OTEL_SERVICE_NAME` | Logical service name for traces | `skill-repeater-service` |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | OTLP gRPC endpoint (Grafana Alloy) | `http://localhost:4317` |
| `OTEL_TRACES_SAMPLER` | OpenTelemetry sampler | `parentbased_traceidratio` |
| `OTEL_TRACES_SAMPLER_ARG` | Sampling ratio | `1.0` |

## Running

### Local (with Docker DB)

```bash
# Start PostgreSQL from root docker-compose
docker compose -f ../docker-compose.dev.yml up -d skill-repeater-db

# Build and run
mvn clean package -DskipTests
java -jar skill-repeater-web/target/skill-repeater-web-*.jar
```

The service starts on port **8210**.

### Swagger UI

Once running, visit [http://localhost:8210/swagger-ui](http://localhost:8210/swagger-ui) for interactive API docs.

## Deployment

Production Docker image uses a multi-stage build (Amazon Corretto 25) and includes JDWP debug support on port 5005.

Kubernetes manifest and deploy scripts are in `deployment/`. Shared cluster config lives in the parent repo's `deployment/` directory.

```bash
./deployment/scripts/build-and-push.sh <version>
./deployment/scripts/deploy.sh <version>
```

### Production

- **API route:** `api.posadskiy.com/skill-repeater/*` (Traefik strips prefix)
- **Database:** Aiven managed PostgreSQL
- **Tracing:** OpenTelemetry OTLP via Grafana Alloy in observability namespace
