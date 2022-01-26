package com.epam.cloudx.pavelsh.awsapp.sns_sqs.service;

import com.epam.cloudx.pavelsh.awsapp.sns_sqs.dto.ImageMetadataDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  void upload(MultipartFile file);

  List<ImageMetadataDto> extractMetadata();

  void delete(String name);

  byte[] download(String name);

  ImageMetadataDto extractRandomMetadata();
}
