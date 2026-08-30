# API Behavior

All user endpoints return JSON. Authenticated requests use the standard `Authorization: Basic ...` header. Example credentials must be supplied through local environment variables.

| Method and path | Role | Success | Notable errors |
| --- | --- | --- | --- |
| `GET /api/status` | Public | `200` status object | — |
| `GET /api/users` | ADMIN | `200` ordered array | `401`, `403` |
| `GET /api/users/{id}` | USER or ADMIN | `200` record | `401`, `403`, `404` |
| `POST /api/users` | ADMIN | `201`, record, Location | `400`, `401`, `403`, `409` |
| `PUT /api/users/{id}` | ADMIN | `200` updated record | `400`, `401`, `403`, `404`, `409` |
| `DELETE /api/users/{id}` | ADMIN | `204` | `401`, `403`, `404` |

Create and update payloads use `firstName`, `lastName`, and `email`. Names must not be blank and email must be valid. Server-generated `id` values cannot be overwritten through the request body.

Application health is available from the Dropwizard administrative connector at `/healthcheck` and should not be exposed publicly without additional controls.
