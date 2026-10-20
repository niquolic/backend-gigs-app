# Étape 1 : build avec Gradle 9.8.1 + JDK 21
FROM gradle:9.8.1-jdk21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar -x test --no-daemon

# Étape 2 : image finale légère, JRE seul (pas besoin de Gradle ni du JDK
# complet pour exécuter l'app : image plus petite, démarrage Cloud Run plus
# rapide, surface d'attaque réduite).
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
