package com.epam.cloudx.pavelsh.awsapp.sns_sqs.controller;

import com.epam.cloudx.pavelsh.awsapp.sns_sqs.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @PostMapping("/subscriptions/{email}")
  public void subscribeEmail(@PathVariable String email) {
    notificationService.subscribeEmail(email);
  }

  @PostMapping("/subscriptions/{email}/apply-filter")
  public void applyFilter(@PathVariable String email) {
    notificationService.applyFilter(email);
  }

  @DeleteMapping("/subscriptions/{email}")
  public void unsubscribeEmail(@PathVariable String email) {
    notificationService.unsubscribeEmail(email);
  }
}
