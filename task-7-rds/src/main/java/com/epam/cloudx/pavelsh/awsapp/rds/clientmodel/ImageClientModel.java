package com.epam.cloudx.pavelsh.awsapp.rds.clientmodel;

import com.amazonaws.services.s3.model.ObjectMetadata;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ImageClientModel {

  private long id;
  private String name;
  private long size;
  private String fileExtension;
  private LocalDateTime updatedAt;
  private byte[] bitmap;
  private ObjectMetadata objectMetadata;
}
