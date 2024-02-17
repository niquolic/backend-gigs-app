# Utilisation d'une image de base avec JDK 17 d'Oracle
FROM gradle:7.6.1-jdk17 AS builder

# Répertoire de travail dans le conteneur
WORKDIR /app

# Copie du build.gradle et des fichiers sources
COPY build.gradle .
COPY src ./src
COPY gradlew .

# Commande pour démarrer l'application Spring Boot
CMD ["./gradlew", "bootRun"]