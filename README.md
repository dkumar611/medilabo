# Medilabo – Microservices Application (Dockerized)

This project is a **Spring Boot microservices application** orchestrated using **Docker Compose**.
All services communicate through a **Spring Cloud Gateway**, and the entire system can be started with a single command.

## Architecture Overview

The application consists of the following services:

| Service                 | Description                                         | Port  |
| ----------------------- | --------------------------------------------------- | ----- |
| **Gateway**             | Entry point for all requests (Spring Cloud Gateway) | 8080  |
| **Frontend**            | UI service                                          | 8082  |
| **Patient Service**     | Manages patient demographics (PostgreSQL)           | 8081  |
| **Notes Service**       | Manages patient notes (MongoDB)                     | 8083  |
| **Diabetes Assessment** | Risk assessment service                             | 8084  |
| **PostgreSQL**          | Database for patient-service                        | 5432  |
| **MongoDB**             | Database for notes-service                          | 27017 |

---

## Project Structure

```
medilabo/
│
├── gateway/
├── frontend/
├── demographics/              # Patient Service
├── noteservice/
├── medilabo-diabetes-assessment/
│
├── docker-compose.yml
└── README.md
```

## Prerequisites

Make sure the following are installed:

* Docker
* Docker Compose (classic `docker-compose`)
* Java 17+ (only if building locally)
* Maven (only if building locally)

---

## Running the Application (Docker)

From the **project root directory**:

```bash
docker-compose up -d
```

This will:

* Build all images
* Create containers
* Start all services
* Create a shared Docker network for service discovery

---

## Verifying Services

### Check running containers

```bash
docker ps
```

### Check compose services

```bash
docker-compose ps
```

---

## Accessing the Application

### UI

```
http://localhost:8080
```

### Gateway APIs

| API                 | Endpoint                                |
| ------------------- | --------------------------------------- |
| Patients            | `http://localhost:8080/api/patients`    |
| Notes               | `http://localhost:8080/api/notes`       |
| Diabetes Assessment | `http://localhost:8080/api/assessments` |

---

## 🧪 Testing Example (Patients API)

```bash
curl http://localhost:8080/api/patients
```

Create a patient:

```bash
curl -X POST http://localhost:8080/api/patients \
-H "Content-Type: application/json" \
-d '{"firstName":"John","lastName":"Doe","gender":"M","address":"123 Main St","phone":"555-1234"}'
```

---

## Docker Networking Notes

* Services communicate using **service names**, not `localhost`
* Example:

  ```
  http://patient-service:8081
  ```
* Docker DNS automatically resolves service names

---

## Configuration

Each service uses the **docker profile**:

```
SPRING_PROFILES_ACTIVE=docker
```

Gateway routes are defined in:

```
gateway/src/main/resources/application-docker.properties
```

---

## Stopping the Application

```bash
docker-compose down
```

To remove volumes (databases):

```bash
docker-compose down -v
```

---

## Troubleshooting

### View logs

```bash
docker-compose logs gateway
```

Live logs:

```bash
docker-compose logs -f gateway
```

### Restart a single service

```bash
docker-compose restart gateway
```
---

## Final Notes
* All services are fully containerized
* Gateway is the **single entry point**
* Databases persist using Docker volumes
* System works consistently across environments

---

**Application is production-ready and Dockerized end-to-end.**
