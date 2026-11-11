package com.example.backendgigsapp.steps;

import com.example.backendgigsapp.AbstractMongoIntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberSpringConfiguration extends AbstractMongoIntegrationTest {
}
