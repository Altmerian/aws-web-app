package com.epam.cloudx.pavelsh.awsapp.sns_sqs.repository.model;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "image_metadata")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageMetadata implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Instant lastUpdateDate;

  @Column(unique = true)
  private String name;

  @Column private Long sizeInBytes;

  @Column private String fileExtension;
}
