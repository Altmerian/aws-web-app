package com.epam.cloudx.pavelsh.awsapp.sns_sqs.service.impl;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.config.properties.ImageBucketProperties;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.controller.ImageController;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.dto.ImageMetadataDto;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.mapper.ImageMetadataMapper;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.repository.ImageMetadataRepository;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.repository.model.ImageMetadata;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.service.ImageService;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.service.NotificationService;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

  private static final String IMAGE_UPLOAD_ACTION = "Image was uploaded";

  private final TransferManager transferManager;
  private final ImageBucketProperties s3Properties;
  private final AmazonS3 s3Client;
  private final ImageMetadataRepository imageMetadataRepository;
  private final ImageMetadataMapper imageMetadataMapper;
  private final NotificationService notificationService;

  @Override
  public void upload(MultipartFile multipartFile) {
    imageMetadataRepository
        .findByName(multipartFile.getOriginalFilename())
        .ifPresent(
            metadata -> {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is already present");
            });
    try {
      var upload =
          transferManager.upload(
              s3Properties.getBucketName(),
              multipartFile.getOriginalFilename(),
              multipartFile.getInputStream(),
              new ObjectMetadata());
      upload.waitForUploadResult();
      var imageMetadata =
          ImageMetadata.builder()
              .fileExtension(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))
              .name(multipartFile.getOriginalFilename())
              .sizeInBytes(multipartFile.getSize())
              .lastUpdateDate(Instant.now())
              .build();
      //      imageMetadataRepository.saveAndFlush(imageMetadata);
      notificationService.sendMessageToQueue(createMessage(imageMetadata));
    } catch (IOException | InterruptedException e) {
      delete(multipartFile.getOriginalFilename());
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  private String createMessage(ImageMetadata imageMetadata) {
    var downloadLink = linkTo(methodOn(ImageController.class).download(imageMetadata.getName()));
    return StringUtils.joinWith(":::", IMAGE_UPLOAD_ACTION, imageMetadata.toString(), downloadLink);
  }

  @Override
  public List<ImageMetadataDto> extractMetadata() {
    return imageMetadataMapper.convertTo(imageMetadataRepository.findAll());
  }

  @Override
  @Transactional
  public void delete(String name) {
    try {
      imageMetadataRepository
          .findByName(name)
          .orElseThrow(
              () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image not found"));
      s3Client.deleteObject(s3Properties.getBucketName(), name);
      imageMetadataRepository.deleteByName(name);
    } catch (AmazonClientException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public byte[] download(String name) {
    try {
      imageMetadataRepository
          .findByName(name)
          .orElseThrow(
              () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image not found"));
      return s3Client
          .getObject(s3Properties.getBucketName(), name)
          .getObjectContent()
          .readAllBytes();
    } catch (IOException | AmazonClientException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public ImageMetadataDto extractRandomMetadata() {
    return imageMetadataMapper.convertTo(imageMetadataRepository.findRandom());
  }
}
