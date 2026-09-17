# TableEase

TableEase is a full-stack restaurant management system that lets customers browse the menu, check table availability, and book a table online. It's built with a Spring Boot REST API backend and a vanilla JavaScript frontend, communicating over a clean, CORS-enabled API.

## Screenshots

*(Add a screenshot of the Menu page, Tables page, and Booking form here once available)*

## Tech Stack

**Backend**
- Java 21
- Spring Boot 4.1
- Spring Data JPA / Hibernate
- MySQL
- Maven

**Frontend**
- HTML5, CSS3, JavaScript (Vanilla, no framework)
- Bootstrap 5

## Architecture Overview

The project follows a decoupled architecture — the backend is a standalone REST API, and the frontend is a separate static site that consumes it over HTTP, communicating across origins via configured CORS rules.

The backend follows a standard layered structure:
- **Model** — JPA entities (`MenuItem`, `DiningTable`, `Booking`) mapped directly to database tables.
- **DTO** — `BookingRequest` decouples the incoming API request shape from the `Booking` entity. This avoids exposing internal entity structure directly to clients and lets the request payload (e.g. a simple `tableId` integer) differ from the entity's actual object relationships (e.g. a full `DiningTable` object).
- **Repository** — Spring Data JPA repositories (`MenuItemRepository`, `DiningTableRepository`, `BookingRepository`) for database access.
- **Service** — Business logic sits here, including existence checks and relationship resolution (e.g. looking up the real `DiningTable` from a `tableId` before creating a `Booking`).
- **Controller** — REST controllers exposing endpoints, with CORS configured globally via `WebConfig`.

The frontend fetches data asynchronously from the backend using the Fetch API, and dynamically renders it into the DOM as Bootstrap-styled cards — no page reloads required.

## Features

- **Menu Browsing** — View all available menu items with pricing, rendered dynamically from the database.
- **Table Availability** — View all restaurant tables with real-time availability status (Available/Reserved).
- **Table Booking** — Book a specific table by submitting customer details, date, time, and guest count through a form.
- **Validation** — Server-side validation rejects invalid input (blank names, non-positive prices, missing table references) with clear error messages.
- **Error Handling** — Centralized exception handling on the backend (`@RestControllerAdvice`) and graceful frontend handling of API/network failures with user-facing error messages instead of silent failures.

## API Endpoints

| Method | Endpoint         | Description                            |
|--------|------------------|-----------------------------------------|
| GET    | `/menu`          | Returns all menu items                  |
| POST   | `/menu`          | Creates a new menu item                 |
| PUT    | `/menu/{id}`     | Updates an existing menu item           |
| DELETE | `/menu/{id}`     | Deletes a menu item                     |
| GET    | `/tables`        | Returns all dining tables with status   |
| POST   | `/tables`        | Creates a new dining table              |
| PUT    | `/tables/{id}`   | Updates an existing table               |
| DELETE | `/tables/{id}`   | Deletes a table                         |
| GET    | `/bookings`      | Returns all bookings                    |
| POST   | `/bookings`      | Creates a new table booking             |
| DELETE | `/bookings/{id}` | Deletes a booking                       |

**Sample POST /bookings request body:**
```json
{
  "tableId": 1,
  "bookingTime": "2026-09-20T19:30:00",
  "customerName": "John Doe",
  "guestCount": 4
}
```

## Project Structure

```
tableease/
├── backend/                # Spring Boot REST API
│   └── src/main/java/com/tableease/tableease/
│       ├── model/           # JPA entities
│       ├── dto/             # Data Transfer Objects
│       ├── repository/      # Spring Data repositories
│       ├── service/         # Business logic
│       ├── controller/      # REST controllers
│       ├── exception/       # Global exception handling
│       └── config/          # CORS / app configuration
├── frontend/                # Static HTML/CSS/JS client
│   ├── index.html            # Menu page
│   ├── tables.html           # Table availability page
│   ├── booking.html          # Booking form
│   ├── css/
│   └── js/
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

> **Note:** If your frontend runs on a different port than `5500`, update the `allowedOrigins` list in `backend/src/main/java/com/tableease/tableease/config/WebConfig.java` to match, or requests will be blocked by CORS.

## Known Limitations / Future Improvements

- No authentication/authorization — anyone can currently create a booking.
- No endpoint to update or cancel an existing booking.
- No double-booking prevention at the API level (a table could theoretically be booked twice for the same slot).
- No automated tests yet.
- Frontend base URL (`http://127.0.0.1:8080`) is hardcoded rather than configurable.

## Roadmap

This project is part of a larger learning path. Planned next steps include JWT-based authentication, password hashing, request validation hardening, and a rebuild of the frontend in React.

## Author

Built by Sneheel Virale as a full-stack learning project, covering Spring Boot REST APIs, JPA/Hibernate relationships, DTO-based request handling, and vanilla JS frontend integration.
