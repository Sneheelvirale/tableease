# TableEase

TableEase is a full-stack restaurant management system that lets customers browse the menu, check table availability, register/log in, and book a table online. It's built with a Spring Boot REST API backend secured with JWT authentication, and a vanilla JavaScript frontend, communicating over a CORS-enabled API.

## Screenshots

*(Add a screenshot of the Menu page, Tables page, Login page, and Booking form here once available)*

## Tech Stack

**Backend**
- Java 21
- Spring Boot 4.1
- Spring Data JPA / Hibernate
- Spring Security + JWT (jjwt)
- MySQL
- Maven

**Frontend**
- HTML5, CSS3, JavaScript (Vanilla, no framework)
- Bootstrap 5

## Architecture Overview

The project follows a decoupled architecture — the backend is a standalone REST API, and the frontend is a separate static site that consumes it over HTTP, with CORS configured through Spring Security's own filter chain (not just Spring MVC, since Spring Security intercepts requests first).

The backend follows a standard layered structure:
- **Model** — JPA entities (`MenuItem`, `DiningTable`, `Booking`, `User`) mapped directly to database tables. `TableStatus` and `Role` are modeled as enums rather than free-text strings, so invalid values can't be persisted.
- **DTO** — Request/response shapes are kept separate from entities. `BookingRequest` decouples the incoming booking payload (a simple `tableId`) from the `Booking` entity's real `DiningTable` relationship. `RegisterRequest`/`LoginRequest` carry only what a client should be able to send; role assignment is deliberately excluded from the request DTO and hardcoded server-side to prevent privilege escalation. `UserResponse`/`AuthResponse` ensure password hashes are never returned to the client.
- **Repository** — Spring Data JPA repositories for database access, including query-derivation methods like `findByUsername`.
- **Service** — Business logic, including relationship resolution (looking up a real `DiningTable` from a `tableId` before creating a `Booking`) and the authentication flow (password hashing/verification, JWT issuance).
- **Controller** — REST controllers exposing endpoints, with `@PreAuthorize` role checks on write operations for `MenuItem` and `DiningTable`.
- **Security** — Password hashing via `BCryptPasswordEncoder`; stateless authentication via signed JWTs, validated on every request by a custom `OncePerRequestFilter`; route-level rules distinguishing public endpoints (browsing menu/tables, auth) from protected ones (bookings, all writes); method-level `@PreAuthorize` for admin-only actions.

The frontend fetches data asynchronously from the backend using the Fetch API, dynamically renders it into the DOM as Bootstrap-styled cards, and stores the JWT in `localStorage` after login, attaching it as a `Authorization: Bearer` header on requests to protected endpoints.

## 🏗️ System Architecture & Design Pattern

This project follows a **Decoupled RESTful Architecture** separating the server logic from the client layer:

* **Backend Architecture (Spring Boot):** 
  * Layered REST API pattern (`Controller` ➔ `Service` ➔ `Repository` ➔ `Entity`).
  * Exposes stateless JSON endpoints.
  * Secured using Spring Security with JWT (JSON Web Tokens) and Role-Based Access Control (RBAC).

* **Frontend Architecture (Client):** 
  * Decoupled Vanilla JavaScript client (`HTML5`, `CSS3/Bootstrap`, `JavaScript ES6`).
  * Consumes backend REST APIs asynchronously using the `fetch` API.
  * Dynamically updates the UI via client-side DOM manipulation.

## Features

- **Menu Browsing** — View all available menu items with pricing, rendered dynamically from the database. Public, no login required.
- **Table Availability** — View all restaurant tables with real-time availability status (Available/Reserved). Public, no login required.
- **User Registration & Login** — Create an account and log in; passwords are hashed (BCrypt), never stored or returned in plain text. Successful login returns a signed JWT used for subsequent authenticated requests.
- **Table Booking** — Book a specific table by submitting customer details, date, time, and guest count. Requires authentication.
- **Role-Based Access Control** — Menu and table management (create/update/delete) is restricted to `ADMIN` accounts; regular `CUSTOMER` accounts can browse and book but not manage inventory.
- **Validation** — Server-side validation rejects invalid input (blank names, non-positive prices, missing table references) with clear error messages.
- **Error Handling** — Centralized exception handling on the backend (`@RestControllerAdvice`) and graceful frontend handling of API/network failures with user-facing error messages instead of silent failures.

## API Endpoints

| Method | Endpoint         | Auth Required     | Description                             |
|--------|------------------|--------------------|------------------------------------------|
| POST   | `/auth/register` | Public             | Create a new account (always `CUSTOMER`) |
| POST   | `/auth/login`    | Public             | Log in, returns a JWT + user info        |
| GET    | `/menu`          | Public             | Returns all menu items                   |
| POST   | `/menu`          | `ADMIN`            | Creates a new menu item                  |
| PUT    | `/menu/{id}`     | `ADMIN`            | Updates an existing menu item            |
| DELETE | `/menu/{id}`     | `ADMIN`            | Deletes a menu item                      |
| GET    | `/tables`        | Public             | Returns all dining tables with status    |
| POST   | `/tables`        | `ADMIN`            | Creates a new dining table               |
| PUT    | `/tables/{id}`   | `ADMIN`            | Updates an existing table                |
| DELETE | `/tables/{id}`   | `ADMIN`            | Deletes a table                          |
| GET    | `/bookings`      | Any logged-in user | Returns all bookings                     |
| POST   | `/bookings`      | Any logged-in user | Creates a new table booking              |
| DELETE | `/bookings/{id}` | Any logged-in user | Deletes a booking                        |

**Sample POST /auth/login response:**
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

**Sample POST /bookings request body:**
```json
{
  "tableId": 1,
  "bookingTime": "2026-09-20T19:30:00",
  "customerName": "John Doe",
  "guestCount": 4
}
```

**Authenticated requests** must include the token from login as a header:
```
Authorization: Bearer <token>
```

## Project Structure

```
tableease/
├── backend/                # Spring Boot REST API
│   └── src/main/java/com/tableease/tableease/
│       ├── model/           # JPA entities (MenuItem, DiningTable, Booking, User, enums)
│       ├── dto/             # Request/response DTOs
│       ├── repository/      # Spring Data repositories
│       ├── service/         # Business logic, including AuthService
│       ├── controller/      # REST controllers
│       ├── exception/       # Global exception handling
│       ├── config/          # Security config, JWT filter, CORS
│       └── util/            # JwtUtil (token generation/validation)
├── frontend/                # Static HTML/CSS/JS client
│   ├── index.html            # Menu page
│   ├── tables.html           # Table availability page
│   ├── booking.html          # Booking form (requires login)
│   ├── login.html            # Login page
│   ├── register.html         # Registration page
│   ├── css/
│   └── js/                   # menu.js, tables.js, booking.js, auth.js
└── README.md
```

## Setup & Run Locally

### Prerequisites
- Java 21+
- Maven
- MySQL Server
- A code editor (Eclipse/STS for backend, VS Code for frontend)
- VS Code Live Server extension (or any static file server)

### 1. Clone the repository
```bash
git clone https://github.com/Sneheelvirale/tableease.git
cd tableease
```

### 2. Set up the database
Create a MySQL database (this project runs MySQL on port `3310` — adjust the port in `application.properties` if your MySQL instance uses a different one, e.g. the default `3306`):
```sql
CREATE DATABASE tableease_db;
```

### 3. Configure environment variables
The backend reads database credentials from environment variables rather than storing them in `application.properties`, so real credentials are never committed to source control.

| Variable      | Description           |
|---------------|------------------------|
| `DB_USERNAME` | Your MySQL username    |
| `DB_PASSWORD` | Your MySQL password    |

Set these as **real operating-system environment variables** so they're visible to any way you choose to run the app:

- **Windows:** Search "Edit environment variables for your account" → New → add `DB_USERNAME` and `DB_PASSWORD` → restart your terminal/IDE afterward so it picks up the change.
- **macOS/Linux:** Add `export DB_USERNAME=yourusername` and `export DB_PASSWORD=yourpassword` to your shell profile (`.bashrc`/`.zshrc`), then restart your terminal.

Alternatively, if running from Eclipse/STS specifically: **Run → Run Configurations → Environment tab** → add both variables there (this only applies when launching from within Eclipse/STS, not from the command line).

### 4. Run the backend

From Eclipse/STS: right-click the project → **Run As → Spring Boot App**.

From the command line (requires the OS-level environment variables from Step 3 to be set):
```bash
cd backend
mvn spring-boot:run
```
The API will start on `http://localhost:8080`.

### 5. Run the frontend
Open `frontend/index.html` with VS Code's Live Server extension (or any static server). It will run on a port like `http://127.0.0.1:5500`.

> **Note:** If your frontend runs on a different port, update the `allowedOrigins` list in `backend/src/main/java/com/tableease/tableease/config/SecurityFilterConfig.java` (in the `corsConfigurationSource` bean) to match, or requests will be blocked by CORS.

### 6. Create an admin account
Every new registration defaults to the `CUSTOMER` role. To create an `ADMIN` account (needed to manage menu items and tables), register a normal account first, then promote it directly in the database:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'yourusername';
```
Log in again afterward to get a fresh token reflecting the new role — an already-issued JWT keeps the role it was created with.

## Known Limitations / Future Improvements

- No endpoint to update an existing booking (create/delete only).
- No double-booking prevention at the API level (a table could theoretically be booked twice for the same slot).
- No automated tests yet.
- Frontend base URLs are hardcoded rather than configurable.
- Unauthenticated requests currently return `403 Forbidden` rather than the more strictly correct `401 Unauthorized` (a known Spring Security nuance with exceptions thrown from `permitAll()` endpoints); functionally equivalent, cosmetic only.
- No admin-only self-service promotion flow — admin roles are currently assigned by direct database update.

## Roadmap

Next stage: rebuilding this system as a microservices architecture — splitting the monolith into independent Menu, Table, Booking, and Auth services behind an API Gateway, containerized with Docker, with inter-service communication, async messaging (RabbitMQ), Redis caching, and centralized observability. The frontend is also planned to be rebuilt in React.

## Author

Built by Sneheel Virale as a full-stack learning project, covering Spring Boot REST APIs, JPA/Hibernate relationships, DTO-based request handling, Spring Security with JWT authentication and role-based access control, and vanilla JS frontend integration.
