package com.epam.cloudx.pavelsh.awsapp.rds.clientmodel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadClientModel {

  @NotEmpty private String name;

  @NotNull private MultipartFile file;
}
