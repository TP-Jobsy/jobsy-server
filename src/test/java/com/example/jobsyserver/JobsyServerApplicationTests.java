package com.example.jobsyserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(TestValidationStubs.class)
class JobsyServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
