package com.epam.cloudx.pavelsh.awsapp.sns_sqs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
    properties = {
      "spring.flyway.enabled = false",
    })
class ApplicationTest {
  @Test
  void contextLoads() {}
}
