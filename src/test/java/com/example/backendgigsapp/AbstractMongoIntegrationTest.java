package com.example.backendgigsapp;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mongodb.MongoDBContainer;

public abstract class AbstractMongoIntegrationTest {

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
