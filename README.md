# record-manager-statistics-server

Backend statistics service for the [Record Manager](https://github.com/kbss-cvut/record-manager) ecosystem.
Exposes a REST API for aggregated statistics over RDF data stored in GraphDB.

**Related repositories**
- [record-manager-statistics](https://github.com/ulcheyev/record-manager-statistics) — frontend dashboard
- [record-manager-ui](https://github.com/kbss-cvut/record-manager-ui) — main Record Manager frontend
- [record-manager](https://github.com/kbss-cvut/record-manager) — Record Manager backend

---

## Requirements

| Tool | Version |
|------|---------|
| Java | 21 |
| Maven | 3.9+ |
| Docker | 24+ |

---

## Getting started

```bash
mvn spring-boot:run
```

Runs with `demo` profile by default — no database or auth required.

---

## Profiles

| Profile | Command | Data source |
|---------|---------|-------------|
| `demo` | `SPRING_ACTIVE_PROFILE=demo` | Mock data |
| `prod` | `SPRING_ACTIVE_PROFILE=prod` | GraphDB via JOPA |

---

## Configuration

All properties are in `application.yml` with environment variable overrides.

---

## Docker

```bash
docker build -t record-manager-statistics-server .
docker run -p 8080:80 record-manager-statistics-server
```