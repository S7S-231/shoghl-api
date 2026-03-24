# Shoghl API 🔧

A REST API marketplace connecting clients with skilled workers (plumbers, electricians, painters, etc.) in Egypt.

## Tech Stack
- Java 17 + Spring Boot 3
- Spring Security + JWT
- PostgreSQL + Flyway
- Docker + GitHub Actions

## Features
- JWT authentication with role-based access (Client / Worker / Admin)
- Complete booking lifecycle (post job → send offer → accept → complete)
- Automated worker rating system
- In-app notification system

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/auth/register | Register |
| POST | /api/v1/auth/login | Login |

### Workers
| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | /api/v1/workers/profile | Create or update profile |
| GET | /api/v1/workers | Search workers by city and category |
| GET | /api/v1/workers/{id} | Get worker profile |
| PATCH | /api/v1/workers/availability | Toggle availability |

### Jobs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/jobs | Post a job |
| GET | /api/v1/jobs | Browse open jobs |
| GET | /api/v1/jobs/my-jobs | Client's own jobs |
| GET | /api/v1/jobs/{id} | Job details |
| PATCH | /api/v1/jobs/{id}/cancel | Cancel a job |

### Bookings
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/bookings | Send an offer |
| PATCH | /api/v1/bookings/{id}/accept | Accept an offer |
| PATCH | /api/v1/bookings/{id}/done | Mark job as done |
| GET | /api/v1/bookings/job/{jobId} | Get offers on a job |
| GET | /api/v1/bookings/my-offers | Worker's own offers |

### Reviews
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/v1/reviews | Submit a review |

### Notifications
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/notifications | Get notifications |
| GET | /api/v1/notifications/unread-count | Unread count |
| PATCH | /api/v1/notifications/{id}/read | Mark as read |

## Running Locally
```bash
# Clone the repo
git clone https://github.com/S7S-231/shoghl-api.git

# Configure application.properties with your PostgreSQL credentials

# Run
./mvnw spring-boot:run
```

## Database
Schema managed by Flyway with 7 migration scripts covering users, worker profiles, categories, jobs, bookings, reviews, and notifications.