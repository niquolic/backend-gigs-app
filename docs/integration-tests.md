# Backend integration tests

The integration suite uses JUnit 5, Cucumber/Gherkin and Tzatziki to exercise the HTTP API against a real Spring Boot application and an isolated MongoDB instance.

## Run locally

Requirements:
- Java 21
- Docker (Testcontainers starts MongoDB automatically)

Run all tests:

```bash
./gradlew test
```

Cucumber scenarios are under `src/test/resources/features`:
- `register.feature`: registration, duplicate login, password validation and default/custom preferences.
- `login.feature`: successful/failed login, JWT response, logout revocation and unauthenticated access.
- `gigs.feature`: gig CRUD and ownership isolation for read/update/delete.
- `user-preferences.feature`: preference updates and authentication requirements.

The MongoDB Testcontainer uses a dedicated `gigs_app_test` database. Tzatziki's MongoDB integration cleans the database between scenarios. The GitHub Actions workflow runs the same Gradle test task for pull requests targeting `master`.

The JWT secret configured for tests is deliberately test-only and must never be reused outside the test profile.
