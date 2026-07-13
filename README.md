# Student Result Management System (SRMS)

A backend-driven web application for managing student records, subject-wise marks, and automatic result generation — built with plain JDBC (no ORM) for full control over SQL and data mapping.

## Features

- Add, view, and delete student records
- Add and remove subject-wise marks for each student
- Server-side validation (required fields, marks not exceeding max, duplicate roll number checks)
- Automatic percentage and grade calculation per student
- Cascading delete — removing a student removes their subject records
- Clean separation of concerns: Controller → Service → DAO → Database

## Tech Stack

**Backend:** Java 17, Spring Boot 3.3.4, Spring JDBC (`JdbcTemplate`)
**Database:** MySQL
**Frontend:** HTML, CSS, JavaScript (served as static resources)
**Build:** Maven

## Why Plain JDBC, Not JPA/Hibernate

This project intentionally avoids Spring Data JPA to get hands-on practice with raw SQL, `PreparedStatement`s, and manual `ResultSet` mapping via `JdbcTemplate` — rather than relying on an ORM to generate queries. DAOs are written explicitly for full visibility into what SQL runs.

## Architecture

```
Controller (REST API)
    ↓
Service (validation, grade/percentage calculation)
    ↓
DAO (JdbcTemplate, raw SQL)
    ↓
MySQL (students, subjects tables)
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|--------------|
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get a student by ID (with subjects, percentage, grade) |
| POST | `/api/students` | Create a student |
| DELETE | `/api/students/{id}` | Delete a student (cascades to subjects) |
| POST | `/api/students/{id}/subjects` | Add a subject/marks entry for a student |
| DELETE | `/api/students/{id}/subjects/{subjectId}` | Remove a subject entry |

## Database Schema

```sql
students (id, name, roll_number [unique])
subjects (id, subject_name, marks_obtained, max_marks, student_id [FK, cascade delete])
```

## Grading Scale

| Percentage | Grade |
|------------|-------|
| ≥ 90 | A+ |
| ≥ 75 | A |
| ≥ 60 | B |
| ≥ 40 | C |
| < 40 | Fail |

## Running Locally

1. Create a MySQL database and update `src/main/resources/application.properties` with your DB URL, username, and password.
2. The schema in `schema.sql` will initialize the `students` and `subjects` tables on startup.
3. Run:
   ```
   mvn spring-boot:run
   ```
4. The app serves the frontend from `src/main/resources/static/` and exposes the API under `/api/students`.

## Project Structure

```
SRMS
├── src/main/java/com/Project/SRMS
│   ├── controller/     # REST controllers
│   ├── service/        # Business logic, validation, grade calculation
│   ├── dao/             # JdbcTemplate data access
│   ├── model/           # Domain entities
│   ├── dto/              # Request/response DTOs
│   └── exception/     # Custom exceptions + global handler
├── src/main/resources
│   ├── static/            # Frontend (HTML/CSS/JS)
│   ├── schema.sql       # DB schema
│   └── application.properties
└── pom.xml
```

## Status

Core backend and frontend are functional: student CRUD, subject/marks CRUD, validation, and grade computation are all working end-to-end.
