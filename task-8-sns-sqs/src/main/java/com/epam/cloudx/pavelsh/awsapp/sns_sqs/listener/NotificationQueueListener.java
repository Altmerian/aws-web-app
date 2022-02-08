package com.epam.cloudx.pavelsh.awsapp.sns_sqs.listener;

import static com.epam.cloudx.pavelsh.awsapp.sns_sqs.service.impl.NotificationServiceImpl.MESSAGE_ATTRIBUTE_NAME;

import com.epam.cloudx.pavelsh.awsapp.sns_sqs.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationQueueListener {

  private final NotificationService notificationService;

//  @Scheduled(fixedRate = 3000)
  public void readBatchFromQueueAndPushToTopic() {
    var messages = notificationService.readMessages();

    messages.forEach(message -> {
      var messageAttributeValueSQS = message.getMessageAttributes().get(MESSAGE_ATTRIBUTE_NAME);
      notificationService.sendMessageToTopic(message.getBody(), messageAttributeValueSQS.getStringValue());
    });
  }
}
