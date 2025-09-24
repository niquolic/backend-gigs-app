# Utilisation d'une image de base avec JDK 17 d'Oracle
FROM gradle:7.6.1-jdk17 AS builder

# Répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers nécessaires dans le conteneur
COPY . .

# Exécuter le build Gradle en ignorant les tests
RUN ./gradlew build -x test

# Exposer le port sur lequel l'application Spring Boot s'exécute
EXPOSE 8080

# Commande pour exécuter l'application Spring Boot lorsque le conteneur démarre
CMD ["java", "-jar", "build/libs/backend-gigs-app-0.0.1-SNAPSHOT.jar"]