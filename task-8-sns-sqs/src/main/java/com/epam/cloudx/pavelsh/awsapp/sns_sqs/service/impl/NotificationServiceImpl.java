package com.epam.cloudx.pavelsh.awsapp.sns_sqs.service.impl;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.AmazonSNSException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.amazonaws.services.sns.model.UnsubscribeRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.config.properties.SNSClientProperties;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.config.properties.SQSClientProperties;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.service.NotificationService;
import com.epam.cloudx.pavelsh.awsapp.sns_sqs.sns.FilterPolicy;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  public static final String MESSAGE_ATTRIBUTE_NAME = "image extension";
  private static final String SNS_PROTOCOL = "email";

  private final SNSClientProperties snsClientProperties;
  private final SQSClientProperties sqsClientProperties;
  private final AmazonSNS snsClient;
  private final AmazonSQS sqsClient;

  @Override
  public void subscribeEmail(String email) {
    try {
      var request =
          new SubscribeRequest()
              .withProtocol(SNS_PROTOCOL)
              .withEndpoint(email)
              .withTopicArn(snsClientProperties.getTopicArn());
      SubscribeResult subscribeResult = snsClient.subscribe(request);
      var filterPolicy = new FilterPolicy();
      filterPolicy.addAttribute(MESSAGE_ATTRIBUTE_NAME, "png");
      filterPolicy.apply(snsClient, subscribeResult.getSubscriptionArn());
    } catch (AmazonSNSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public void unsubscribeEmail(String email) {
    try {
      var listResult = snsClient.listSubscriptionsByTopic(snsClientProperties.getTopicArn());
      var subscriptions = listResult.getSubscriptions();
      subscriptions.stream()
          .filter(subscription -> email.equals(subscription.getEndpoint()))
          .findAny()
          .ifPresent(subscription -> unsubscribe(subscription.getSubscriptionArn()));
    } catch (AmazonSNSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public void sendMessageToQueue(String message, String attributeValue) {
    try {
      var messageAttributeValueSQS = new MessageAttributeValue().withStringValue(attributeValue);
      var request =
          new SendMessageRequest()
              .withQueueUrl(sqsClientProperties.getQueueUrl())
              .withMessageBody(message)
              .withMessageAttributes(Map.of(MESSAGE_ATTRIBUTE_NAME, messageAttributeValueSQS))
              .withDelaySeconds(5);
      sqsClient.sendMessage(request);
    } catch (AmazonSQSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public void sendMessageToTopic(String message, String attributeValue) {
    try {
      var messageAttributeValueSNS =
          new com.amazonaws.services.sns.model.MessageAttributeValue()
              .withStringValue(attributeValue);
      var publishRequest =
          new PublishRequest()
              .withMessage(message)
              .withMessageAttributes(Map.of(MESSAGE_ATTRIBUTE_NAME, messageAttributeValueSNS))
              .withTopicArn(snsClientProperties.getTopicArn());
      snsClient.publish(publishRequest);
    } catch (AmazonSNSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  @Override
  public List<Message> readMessages() {
    try {
      var queueUrl = sqsClientProperties.getQueueUrl();
      var request =
          new ReceiveMessageRequest()
              .withQueueUrl(queueUrl)
              .withWaitTimeSeconds(10)
              .withMaxNumberOfMessages(10);
      var messages = sqsClient.receiveMessage(request).getMessages();
      messages.stream()
          .map(Message::getReceiptHandle)
          .forEach(receipt -> sqsClient.deleteMessage(queueUrl, receipt));
      return messages;
    } catch (AmazonSQSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }

  private void unsubscribe(String subscriptionArn) {
    try {
      var unsubscribeRequest = new UnsubscribeRequest().withSubscriptionArn(subscriptionArn);
      snsClient.unsubscribe(unsubscribeRequest);
    } catch (AmazonSNSException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }
  }
}
