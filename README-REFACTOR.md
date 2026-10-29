# Production-readiness refactor

## Changes

- Added MapStruct-based mapping between gig request/response DTOs and MongoDB entities.
- Controllers delegate request handling to services; mapping and business logic live outside controllers.
- Gig ownership always comes from the authenticated principal. Create requests cannot overwrite existing documents, and updates preserve the persisted owner and ID.
- Replaced field injection with constructor injection in the touched controllers and services.
- Added request validation and centralized API errors using Spring ProblemDetail.
- Removed duplicate Gradle test-task configuration and redundant JJWT dependency declarations.
- JWT startup fails fast when the configured secret is shorter than 32 bytes. Supply a strong random `JWT_SECRET` through a secret manager.
- Configuration examples do not contain a sample secret.

## Compatibility and rollout notes

- Existing endpoint paths are preserved.
- Gig creation returns HTTP 201 and deletion returns HTTP 204; verify that the React Native client does not expect a response body for these operations.
- Error responses use a ProblemDetail JSON object.
- New login/register requests require passwords between 10 and 72 characters. Review this constraint against existing accounts before deployment.
- JWT lifetime defaults to 15 minutes. Review login/refresh UX before rollout.
- Existing plaintext passwords are upgraded to BCrypt after a successful login.

## Data model

Gigs are stored in the separate `gigs` collection and linked to users by `userId`. No schema change was made in this refactor.

## Before production

1. Set `MONGODB_URI`, `MONGODB_DATABASE`, and a random `JWT_SECRET` in a secret manager.
2. Verify the unique MongoDB index on `users.login` exists in the deployed database.
3. Run `./gradlew clean test bootJar` with JDK 21.
4. Test the mobile app against the success status codes and ProblemDetail error responses.
5. Review CORS origins, Cloud Run ingress/IAM, rate limiting, backups, monitoring, and token refresh policy.
6. Run integration tests against a disposable MongoDB instance.

This refactor still requires a successful build, automated tests, and a deployment review before it should be considered production-ready.
