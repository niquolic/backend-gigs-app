package com.example.backendgigsapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BackendGigsAppApplicationTests extends AbstractMongoIntegrationTest {

    @Test
    void contextLoads() {
    }
}
