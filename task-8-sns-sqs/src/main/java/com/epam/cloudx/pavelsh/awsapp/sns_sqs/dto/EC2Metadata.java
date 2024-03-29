package com.epam.cloudx.pavelsh.awsapp.sns_sqs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EC2Metadata {

  private String region;
  private String availabilityZone;
}
