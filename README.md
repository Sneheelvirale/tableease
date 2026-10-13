# TableEase

TableEase is a full-stack restaurant management system that lets customers browse the menu, check table availability, register/log in, and book a table online.

The project exists in two forms inside this repository:

1. **`backend/` — the original monolith.** A single Spring Boot REST API secured with JWT, serving a vanilla JavaScript frontend.
2. **Microservices version (in progress).** The monolith is being split into independent services (`auth-service`, `menu-service`, `order-service`) behind an `api-gateway`, with `eureka-server` for service discovery, all orchestrated with `docker-compose.yml`.

> **Status:** The monolith is complete and documented below. The microservices migration is under active development, so some services and features may be incomplete.

## Screenshots

*(Add screenshots of the Menu page, Tables page, Login page, and Booking form here.)*

## Tech Stack

**Backend**
- Java 21
- Spring Boot 4.1
- Spring Data JPA / Hibernate
- Spring Security + JWT (jjwt)
- MySQL
- Maven

**Microservices layer**
- Spring Cloud Gateway (`api-gateway`)
- Netflix Eureka (`eureka-server`)
- Docker & Docker Compose

**Frontend**
- HTML5, CSS3, JavaScript (vanilla, no framework)
- Bootstrap 5

## Repository Structure

```
tableease/
├── .vscode/                 # Editor settings
├── api-gateway/             # Single entry point; routes client requests to services
├── auth-service/            # Registration, login, JWT issuance
├── backend/                 # Original monolithic Spring Boot REST API
├── eureka-server/           # Service discovery registry
├── frontend/                # Static HTML/CSS/JS client
├── menu-service/            # Menu item management
├── order-service/           # Order management
├── docker-compose.yml       # Runs the microservices stack
├── .gitignore
└── README.md
```

### Monolith layout (`backend/`)

```
backend/src/main/java/com/tableease/tableease/
├── model/        # JPA entities (MenuItem, DiningTable, Booking, User, enums)
├── dto/          # Request/response DTOs
├── repository/   # Spring Data repositories
├── service/      # Business logic, including AuthService
├── controller/   # REST controllers
├── exception/    # Global exception handling
├── config/       # Security config, JWT filter, CORS
└── util/         # JwtUtil (token generation/validation)
```

### Frontend layout (`frontend/`)

```
frontend/
├── index.html        # Menu page
├── tables.html       # Table availability page
├── booking.html      # Booking form (requires login)
├── login.html        # Login page
├── register.html     # Registration page
├── css/
└── js/               # menu.js, tables.js, booking.js, auth.js
```

## Architecture Overview

### Monolith

The backend is a standalone REST API and the frontend is a separate static site that consumes it over HTTP. CORS is configured through Spring Security's own filter chain (not just Spring MVC, since Spring Security intercepts requests first).

The backend follows a standard layered structure (`Controller` ➔ `Service` ➔ `Repository` ➔ `Entity`):

- **Model:** JPA entities (`MenuItem`, `DiningTable`, `Booking`, `User`). `TableStatus` and `Role` are enums, so invalid values can't be persisted.
- **DTO:** Request/response shapes are kept separate from entities. `BookingRequest` decouples the incoming payload (a simple `tableId`) from the `Booking` entity's real `DiningTable` relationship. `RegisterRequest`/`LoginRequest` carry only what a client should be able to send; the role is assigned server-side to prevent privilege escalation. `UserResponse`/`AuthResponse` ensure password hashes are never returned.
- **Repository:** Spring Data JPA repositories, including derived queries like `findByUsername`.
- **Service:** Business logic, including relationship resolution (looking up a real `DiningTable` from a `tableId` before creating a `Booking`) and the authentication flow (password hashing, JWT issuance).
- **Controller:** REST endpoints with `@PreAuthorize` role checks on write operations for `MenuItem` and `DiningTable`.
- **Security:** `BCryptPasswordEncoder` for passwords; stateless authentication via signed JWTs validated on every request by a custom `OncePerRequestFilter`; route-level rules separating public endpoints from protected ones; method-level `@PreAuthorize` for admin-only actions.

### Microservices (in progress)

```
                    ┌────────────────┐
   Frontend ──────▶ │  API Gateway   │
                    └───────┬────────┘
              ┌─────────────┼──────────────┐
              ▼             ▼              ▼
        auth-service   menu-service   order-service
              └─────────────┬──────────────┘
                            ▼
                     Eureka Server
                  (service discovery)
```

- **API Gateway:** single public entry point that routes requests to the right service.
- **Eureka Server:** services register themselves, so the gateway and other services find each other by name rather than hardcoded addresses.
- **Auth Service:** handles registration, login and JWT issuance.
- **Menu Service:** menu item CRUD.
- **Order Service:** order handling.
- **Docker Compose:** builds and runs the whole stack together.

The frontend fetches data with the Fetch API, renders it into the DOM as Bootstrap cards, and stores the JWT in `localStorage` after login, sending it as an `Authorization: Bearer` header on protected requests.

## Features

- **Menu Browsing:** View all menu items with pricing, rendered dynamically from the database. Public, no login required.
- **Table Availability:** View all tables with status (Available/Reserved). Public, no login required.
- **User Registration & Login:** Passwords are hashed with BCrypt, and a successful login returns a signed JWT.
- **Table Booking:** Book a specific table by submitting customer details, date/time, and guest count. Requires authentication.
- **Role-Based Access Control:** Menu and table management is restricted to `ADMIN` accounts; `CUSTOMER` accounts can browse and book.
- **Validation:** Server-side validation rejects invalid input (blank names, non-positive prices, missing table references) with clear messages.
- **Error Handling:** Centralized backend handling (`@RestControllerAdvice`) and graceful frontend handling of API/network failures.

## API Endpoints (monolith)

| Method | Endpoint         | Auth Required      | Description                              |
|--------|------------------|--------------------|------------------------------------------|
| POST   | `/auth/register` | Public             | Create a new account (always `CUSTOMER`) |
| POST   | `/auth/login`    | Public             | Log in, returns a JWT + user info        |
| GET    | `/menu`          | Public             | Returns all menu items                   |
| POST   | `/menu`          | `ADMIN`            | Creates a new menu item                  |
| PUT    | `/menu/{id}`     | `ADMIN`            | Updates a menu item                      |
| DELETE | `/menu/{id}`     | `ADMIN`            | Deletes a menu item                      |
| GET    | `/tables`        | Public             | Returns all tables with status           |
| POST   | `/tables`        | `ADMIN`            | Creates a new table                      |
| PUT    | `/tables/{id}`   | `ADMIN`            | Updates a table                          |
| DELETE | `/tables/{id}`   | `ADMIN`            | Deletes a table                          |
| GET    | `/bookings`      | Any logged-in user | Returns all bookings                     |
| POST   | `/bookings`      | Any logged-in user | Creates a new table booking              |
| DELETE | `/bookings/{id}` | Any logged-in user | Deletes a booking                        |

**Sample `POST /auth/login` response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "username": "testuser",
    "role": "CUSTOMER"
  }
}
```

**Sample `POST /bookings` request body:**
```json
{
  "tableId": 1,
  "bookingTime": "2026-09-20T19:30:00",
  "customerName": "John Doe",
  "guestCount": 4
}
```

**Authenticated requests** must include the token from login:
```
Authorization: Bearer <token>
```

## Setup & Run Locally

### Prerequisites
- Java 21+
- Maven
- MySQL Server
- Docker & Docker Compose (for the microservices stack)
- A code editor (Eclipse/STS for backend, VS Code for frontend)
- VS Code Live Server extension (or any static file server)

### 1. Clone the repository
```bash
git clone https://github.com/Sneheelvirale/tableease.git
cd tableease
```

### 2. Set up the database
Create a MySQL database (this project runs MySQL on port `3310`; adjust the port in `application.properties` if yours differs, e.g. the default `3306`):
```sql
CREATE DATABASE tableease_db;
```

### 3. Configure environment variables
Database credentials are read from environment variables, so real credentials are never committed.

| Variable      | Description         |
|---------------|---------------------|
| `DB_USERNAME` | Your MySQL username |
| `DB_PASSWORD` | Your MySQL password |

Set these as real operating-system environment variables:

- **Windows:** Search "Edit environment variables for your account" → New → add both → restart your terminal/IDE.
- **macOS/Linux:** Add `export DB_USERNAME=...` and `export DB_PASSWORD=...` to `.bashrc`/`.zshrc`, then restart your terminal.
- **Eclipse/STS only:** Run → Run Configurations → Environment tab (applies only when launching from the IDE).

### Option A: Run the monolith

```bash
cd backend
mvn spring-boot:run
```
The API starts on `http://localhost:8080`. Then open `frontend/index.html` with Live Server (typically `http://127.0.0.1:5500`).

> If your frontend runs on a different port, update `allowedOrigins` in `backend/src/main/java/com/tableease/tableease/config/SecurityFilterConfig.java` (`corsConfigurationSource` bean), or requests will be blocked by CORS.

### Option B: Run the microservices stack

```bash
docker-compose up --build
```

Start order and ports are defined in `docker-compose.yml`. A typical order is:
1. `eureka-server`
2. `auth-service`, `menu-service`, `order-service`
3. `api-gateway`

Check the Eureka dashboard to confirm each service has registered, then point the frontend at the gateway's URL.

<!-- TODO: fill in the actual ports from docker-compose.yml, e.g. Eureka dashboard, gateway, each service -->

### Create an admin account
Every new registration defaults to `CUSTOMER`. Register a normal account, then promote it in the database:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'yourusername';
```
Log in again afterward to get a fresh token. An already-issued JWT keeps the role it was created with.

## Known Limitations / Future Improvements

**Functionality**
- No endpoint to update an existing booking (create/delete only).
- No double-booking prevention at the API level; a table could be booked twice for the same slot.
- No admin self-service promotion flow; roles are changed by direct database update.

**Security**
- `GET /bookings` returns all bookings and `DELETE /bookings/{id}` is open to any logged-in user. Bookings should be scoped to their owner, with admin override.
- The JWT is stored in `localStorage`, which is exposed to XSS. HttpOnly cookies are a safer alternative.
- Unauthenticated requests return `403 Forbidden` instead of `401 Unauthorized` (a known Spring Security nuance; cosmetic only).

**Engineering**
- No automated tests yet.
- Frontend base URLs are hardcoded rather than configurable.
- Documentation for each microservice is still to be written.

## Roadmap

- [x] Layered monolith with JWT authentication and RBAC
- [x] Service skeletons: API Gateway, Eureka, Auth, Menu, Order
- [x] Docker Compose setup
- [ ] Dedicated Table and Booking services
- [ ] Inter-service communication and async messaging (RabbitMQ)
- [ ] Redis caching
- [ ] Centralized observability (logging, metrics, tracing)
- [ ] Automated tests (unit + integration)
- [ ] Frontend rebuilt in React

## Author

Built by Sneheel Virale as a full-stack learning project, covering Spring Boot REST APIs, JPA/Hibernate relationships, DTO-based request handling, Spring Security with JWT and role-based access control, microservices with Eureka and an API gateway, Docker, and vanilla JS frontend integration.
