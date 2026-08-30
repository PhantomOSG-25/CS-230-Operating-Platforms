# Support Runbook

## Service checks

1. Confirm `GET /api/status` returns `200` with `status: ready`.
2. From the protected administrative network, confirm `/healthcheck` reports `user-repository` as healthy.
3. Review application logs for startup configuration errors, validation failures, or repeated authorization failures. Never paste credentials into logs or tickets.

## Common incidents

### Service does not start

- Confirm Java 21 is active.
- Confirm both required password environment variables are present.
- Confirm application and admin ports are available.
- Validate the YAML structure and account roles.

### Requests return 401

- Confirm the client sends a Basic authorization header.
- Confirm the configured username and corresponding environment-supplied password.
- Rotate the local password if it may have been disclosed.

### Requests return 403

Authentication succeeded, but the principal lacks the endpoint role. Consult the access-policy table instead of broadening permissions ad hoc.

### Data disappears after restart

This is expected: the portfolio repository is in-memory. Reconfigure seed users or implement the documented repository seam with durable storage.

## Recovery and escalation

Restarting restores configured seed data but discards runtime changes. Capture the time, endpoint, response status, correlation evidence available in logs, and steps to reproduce. Do not capture authorization headers, passwords, or personal data. Escalate security concerns privately.
