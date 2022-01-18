package com.epam.cloudx.pavelsh.awsapp.rds.controller;

import com.epam.cloudx.pavelsh.awsapp.rds.clientmodel.ImageUploadClientModel;
import com.epam.cloudx.pavelsh.awsapp.rds.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
public class ImageController {

  @Autowired private ImageService imageService;

  @GetMapping
  public ResponseEntity<?> getAll() {
    return imageService.findAll();
  }

  @PostMapping("/upload")
  public ResponseEntity<?> uploadImage(@ModelAttribute ImageUploadClientModel downloadClientModel) {
    return imageService.upload(downloadClientModel);
  }

  @DeleteMapping("/delete/{name}")
  public ResponseEntity<?> delete(@PathVariable String name) {
    return imageService.delete(name);
  }

  @GetMapping("/get-random")
  public ResponseEntity<?> getRandomImage() {
    return imageService.getRandom();
  }

  @GetMapping(value = "/download/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
  public byte[] download(@PathVariable String name) {
    return imageService.getImage(name);
  }
}
