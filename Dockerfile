# Stage 1: Build with Gradle 9.8.1 and JDK 21.
FROM gradle:9.8.1-jdk21 AS builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Use a smaller JRE-only image to reduce image size and attack surface.
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
