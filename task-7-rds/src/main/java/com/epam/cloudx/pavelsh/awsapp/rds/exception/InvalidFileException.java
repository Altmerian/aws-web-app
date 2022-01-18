package com.epam.cloudx.pavelsh.awsapp.rds.exception;

public class InvalidFileException extends RuntimeException {
  public InvalidFileException() {}

  public InvalidFileException(String message) {
    super(message);
  }
}
