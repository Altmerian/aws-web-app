package com.epam.cloudx.pavelsh.awsapp.sns_sqs.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageMetadataDto {

  private Instant lastUpdateDate;
  private String name;
  private Long size;
  private String fileExtension;
}
