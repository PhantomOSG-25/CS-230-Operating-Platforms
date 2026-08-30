# Modernization Decisions

This implementation is a reconstruction of an earlier course prototype, not a cosmetic reformat of the historical submission.

| Earlier prototype concern | Portfolio implementation |
| --- | --- |
| Java 8 and Dropwizard 2-era layout | Java 21 and Dropwizard 5 structure |
| Shared password embedded in source | Environment-substituted local credentials |
| Random principal identifiers | Stable configuration-backed principal name |
| Contradictory or missing endpoint roles | Documented role matrix enforced with annotations |
| Public mutable map | Private concurrent repository behind an interface |
| Client-controlled or confused IDs | Server-generated IDs and path-authoritative updates |
| Reversed create behavior | `201 Created` only for a new valid record |
| Unprotected deletion | ADMIN-only delete with `204` or `404` |
| Health check calling hard-coded localhost | Direct repository health check |
| No tests or CI | Unit and real HTTP tests, coverage gates, formatting, CI |
| Binaries and personal course documents at repository root | Source-first branch with provenance and reviewer-facing Markdown |

The raw course artifacts remain in prior Git history so the historical record is preserved. They are not duplicated in the current tree because they obscure the code, include personal/course metadata, and include generated binaries or archives.
