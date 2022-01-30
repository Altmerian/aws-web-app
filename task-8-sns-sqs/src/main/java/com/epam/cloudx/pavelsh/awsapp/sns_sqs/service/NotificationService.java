package com.epam.cloudx.pavelsh.awsapp.sns_sqs.service;

import com.amazonaws.services.sqs.model.Message;
import java.util.List;

public interface NotificationService {

  void subscribeEmail(String email);

  void unsubscribeEmail(String email);

  void sendMessageToQueue(String message, String attributeValue);

  void sendMessageToTopic(String message, String attributeValue);

  List<Message> readMessages();

  void applyFilter(String email);
}
