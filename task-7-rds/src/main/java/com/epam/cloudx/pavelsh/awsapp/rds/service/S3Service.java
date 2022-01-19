package com.epam.cloudx.pavelsh.awsapp.rds.service;

import static com.epam.cloudx.pavelsh.awsapp.rds.constant.Constants.S3_BUCKET_NAME;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.epam.cloudx.pavelsh.awsapp.rds.exception.S3ObjectNotFoundException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class S3Service {

  @Autowired private AmazonS3 s3;

  @Autowired private FileService fileService;

  public byte[] downloadObject(String objectName) {
    checkBucketExists();
    checkObjectExits(objectName);

    S3Object o = s3.getObject(S3_BUCKET_NAME, objectName);
    return fileService.readBitmap(o);
  }

  public void uploadObject(InputStream file, String filename, String customName) {
    checkBucketExists();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.addUserMetadata("Name", filename);
    metadata.setContentType("image/jpg");
    PutObjectRequest request = new PutObjectRequest(S3_BUCKET_NAME, customName, file, metadata);
    request.setMetadata(metadata);
    s3.putObject(request);
  }

  public void deleteObject(String objectName) {
    checkBucketExists();
    checkObjectExits(objectName);
    s3.deleteObject(S3_BUCKET_NAME, objectName);
  }

  private void checkBucketExists() {
    if (!s3.doesBucketExistV2(S3_BUCKET_NAME)) {
      var createBucketRequest = new CreateBucketRequest(S3_BUCKET_NAME, Region.EU_Frankfurt);
      s3.createBucket(createBucketRequest);
    }
  }

  private void checkObjectExits(String objectName) {
    if (!s3.doesObjectExist(S3_BUCKET_NAME, objectName)) {
      throw new S3ObjectNotFoundException();
    }
  }
}
