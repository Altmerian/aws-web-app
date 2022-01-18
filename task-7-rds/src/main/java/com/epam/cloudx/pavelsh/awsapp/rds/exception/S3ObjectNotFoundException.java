package com.epam.cloudx.pavelsh.awsapp.rds.exception;

public class S3ObjectNotFoundException extends RuntimeException {

  public S3ObjectNotFoundException() {}

  public S3ObjectNotFoundException(String message) {
    super(message);
  }
}
