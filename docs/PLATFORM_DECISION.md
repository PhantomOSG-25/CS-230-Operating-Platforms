# Platform Decision

## Recommendation

For a small networked game service, deploy the stateless Java application in a Linux container while allowing clients to remain platform-independent through the HTTP API.

## Decision drivers

| Driver | Linux container | Windows Server | macOS server |
| --- | --- | --- | --- |
| Java runtime support | Strong | Strong | Strong |
| Hosting cost and density | Strong | Moderate | Weak |
| Container ecosystem | Strong | Moderate | Moderate |
| Automated deployment tooling | Strong | Strong | Moderate |
| Operational familiarity required | Moderate | Moderate | Moderate |

Linux is recommended because it provides broad Java support, efficient commodity hosting, mature container tooling, and a clear path from local development to managed infrastructure. Windows Server remains reasonable where an organization already standardizes on Microsoft operations. macOS is appropriate for client development but is a poor default for commodity server hosting.

## Architecture implications

- Keep clients thin and communicate through versioned HTTP contracts.
- Package the service as an immutable artifact or container.
- Externalize credentials and environment-specific configuration.
- Store durable data outside the application process.
- Terminate TLS at a managed ingress or reverse proxy.
- Observe the administrative health endpoint privately.

This decision is scoped to the demonstrated service. A real selection would also measure projected traffic, latency, staff expertise, compliance requirements, and total cost.
