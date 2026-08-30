# Security Policy

## Supported scope

This is an educational portfolio service. It is designed for local review and does not receive production security updates or operate as an identity provider.

## Controls demonstrated

- Credentials are supplied through environment substitution and are absent from source control.
- Authentication produces a stable principal with explicit roles.
- Administrative operations require the `ADMIN` role; read-by-ID accepts `USER` or `ADMIN`.
- API records never contain passwords.
- Validation rejects blank names and malformed email addresses.
- Duplicate email addresses produce a controlled conflict response.
- Health checks inspect the repository directly instead of making a self-referential HTTP request.
- Error responses avoid stack traces and internal implementation details.

## Threat boundaries

Basic authentication encodes rather than encrypts credentials. Use the example service only on localhost. Any deployment beyond localhost must terminate TLS before traffic reaches the application. The in-memory repository provides neither durable storage nor encryption at rest.

Before production use, replace Basic authentication with an established identity provider, use password hashing or token validation, add rate limiting and audit events, persist data in a protected database, manage secrets in a dedicated secret store, and conduct an independent security review.

## Reporting

Do not include live credentials or personal information in an issue. Use GitHub's private vulnerability-reporting feature if it is enabled for the repository.
