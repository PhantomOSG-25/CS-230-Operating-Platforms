# Test Strategy

The test suite follows the boundaries in the architecture rather than testing only getters and setters.

- Repository tests verify stable IDs, sorting, update/delete behavior, missing records, and case-insensitive email uniqueness.
- Authentication tests verify valid credentials, invalid credentials, unknown users, stable principals, and role decisions.
- Health-check tests verify direct repository observability.
- The application integration test starts Dropwizard on an ephemeral port and verifies public status, `401` authentication, `403` authorization, and the full create/read/update/delete lifecycle over HTTP.

`mvn verify` enforces Java and Maven versions, runs all tests, checks formatting, builds the executable JAR, produces a JaCoCo report, and fails below the configured line and branch coverage thresholds. GitHub Actions runs the same command for pushes to `main` and pull requests.

The tests use only synthetic `example.invalid` records and local test credentials. They do not call external services or require a persistent database.
