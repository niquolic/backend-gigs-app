package com.example.backendgigsapp.steps;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import io.cucumber.spring.CucumberContextConfiguration;
import org.testcontainers.mongodb.MongoDBContainer;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    private static final MongoDBContainer MONGO = new MongoDBContainer("mongo:7.0");

    static {
        MONGO.start();
        Runtime.getRuntime().addShutdownHook(new Thread(MONGO::stop));
    }

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", () -> MONGO.getReplicaSetUrl("gigs_app_test"));
        registry.add("spring.mongodb.database", () -> "gigs_app_test");
        registry.add("jwt.secret", () -> "test-only-secret-key-with-at-least-32-bytes");
        registry.add("jwt.expirationMs", () -> "900000");
    }
}
