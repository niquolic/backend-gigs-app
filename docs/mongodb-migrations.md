# MongoDB migrations with Flyway

Flyway's MongoDB native connector is used through the Flyway CLI (v11 or later), rather than through Spring Boot's JDBC-oriented auto-configuration. JSON migrations use the native MongoDB API and do not require `mongosh`.

## Prerequisites

- Flyway CLI v11 or later, with MongoDB support enabled in the installed distribution.
- Access to the target MongoDB database.
- A MongoDB account with permissions to create indexes and write Flyway's schema history collection.

## Configure the connection

The committed `flyway.toml` targets the local database `GigsAppDB` at `mongodb://localhost:27017`. Do not commit production credentials or connection strings.

For another environment, update the URL in a local, uncommitted configuration file or override the connection settings with Flyway's supported environment variables. Keep credentials in environment variables or a secret manager.

## Run migrations

From the repository root:

```bash
flyway info
flyway validate
flyway migrate
```

Flyway records executed migrations in its MongoDB schema-history collection. Later migrations should use the next version number and should not modify migrations that have already run.

## Existing database

The current `GigsAppDB` database already contains `gigs` and `users`. The initial migrations add non-unique indexes on `gigs.userId` and `users.login`; they do not recreate or delete collections or documents.

Because this is an existing database with no Flyway history yet, review the target database before the first `migrate` run. Back up important data first. These migrations are additive and do not attempt to recreate the existing Compass collection validators.

## User preferences

The `V3` migration creates `user_preferences` with required `userId`, `language`, and `currency` fields. `userId` is an ObjectId referencing a document in `users`; MongoDB does not enforce that reference as a foreign key. The `V4` migration adds a unique index on `userId`, so a user can have at most one preferences document.

Language accepts a two-letter lowercase code (such as `fr`, `en`, `es`) and optionally a region (such as `fr-FR`). Currency accepts a three-letter uppercase code (such as `EUR`, `USD`). These patterns check the format, not whether a code is officially supported. Set defaults such as `fr` and `EUR` in application code when creating a user's preferences; the migration does not insert a preferences document for every existing user or automatically assign field defaults.

## Migration format

This project uses JSON migrations only. Do not mix JavaScript and JSON migrations in the same Flyway project. JSON migrations are executed through Flyway's MongoDB native connector.
