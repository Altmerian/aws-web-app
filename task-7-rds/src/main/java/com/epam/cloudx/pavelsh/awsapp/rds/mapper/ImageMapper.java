package com.epam.cloudx.pavelsh.awsapp.rds.mapper;

import com.epam.cloudx.pavelsh.awsapp.rds.clientmodel.ImageClientModel;
import com.epam.cloudx.pavelsh.awsapp.rds.clientmodel.ImageUploadClientModel;
import com.epam.cloudx.pavelsh.awsapp.rds.entity.ImageEntityModel;
import com.epam.cloudx.pavelsh.awsapp.rds.service.S3Service;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ImageMapper {

  @Mapping(target = "bitmap", ignore = true)
  ImageClientModel toClientModel(ImageEntityModel entityModel, @Context S3Service s3Service);

  ImageEntityModel toEntityModel(ImageUploadClientModel clientModel);

  ImageEntityModel toEntityModel(ImageClientModel clientModel);

  @AfterMapping
  default void setBitmapToClient(
      @MappingTarget ImageClientModel target,
      ImageEntityModel source,
      @Context S3Service s3Service) {
    target.setBitmap(s3Service.downloadObject(source.getName()));
  }
}
