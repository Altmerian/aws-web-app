package com.epam.cloudx.pavelsh.awsapp.sns_sqs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "lambda")
@Component
public class LambdaProperties {

  private String functionName;
  private String region;
}
