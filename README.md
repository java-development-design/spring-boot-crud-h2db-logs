# Spring Boot CRUD (H2) — Java 21

A minimal, production-grade Spring Boot CRUD REST API using **Java 21**, **Spring Boot 3.4.9**, **Maven**, and **H2** in‑memory database.

## Quick Start

Prereqs:
- JDK **21**
- Maven **3.9+**

Run:

```bash
mvn spring-boot:run
# or build a jar
java -jar target/spring-boot-crud-h2db-logs-0.0.1-SNAPSHOT.jar
```

API:
- `GET    /employee-service/api/employees`
- `GET    /employee-service/api/employees/{id}`
- `POST   /employee-service/api/employees/save` (JSON body: `{ "name": "...", "email": "...", "role": "..." }`)
- `PUT    /employee-service/api/employees/{id}`
- `DELETE /employee-service/api/employees/{id}`

H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:demo`, user: `sa`, password empty).

