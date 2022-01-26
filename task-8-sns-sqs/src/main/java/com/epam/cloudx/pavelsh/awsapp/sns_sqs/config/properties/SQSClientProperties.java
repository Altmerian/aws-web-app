package com.epam.cloudx.pavelsh.awsapp.sns_sqs.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "sqs")
@Component
public class SQSClientProperties {

  private String region;
  private String queueUrl;
}
