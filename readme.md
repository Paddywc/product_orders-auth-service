# Auth Service

The **Auth Service** is the platform’s authentication provider. It:
- Registers users and handles login
- Issues **JWT access tokens**
- Publishes its **public signing keys** via a **JWKS** endpoint so other services can verify JWTs without sharing secrets

## Architecture Role

- **Issuer:** this service signs JWTs (private key stays here)
- **Validators (resource servers):** other services validate JWT signatures by downloading the JWKS from this service

All protected endpoints in other services require the JWT token: `Authorization: Bearer <token>`.

## Tech Stack

| Technology      | Purpose               |
| --------------- | --------------------- |
| Java 17         | Runtime               |
| Spring Boot     | Application framework |
| Spring Security | Authentication        |
| JWT (Nimbus)    | Token generation      |
| Spring Data JPA | Database access       |
| MySQL           | User storage          |
| Flyway          | Database migrations   |
| Docker          | Containerization      |

## Environment Variables

An example list of environment variables is found in [`.env.example`](.env.example).

## Running the Service

Run the service using `docker-compose up --build`. The service will be available at `http://localhost:8083`.

## HTTP Endpoints

- `POST /api/auth/register` — Create a user account
- `POST /api/auth/login` — Authenticate and receive a JWT
- `GET  /.well-known/jwks.json` — JWKS (public keys for JWT verification)
- `GET  /actuator/**` — Health/metrics endpoints (if enabled)

## JWT + JWKS

- Clients authenticate once (login) and receive a JWT.
- Clients include the token on subsequent calls:

## User storage & passwords

- Users are stored in a database via Spring Security’s JDBC user store.
- Passwords are hashed using **BCrypt**.

