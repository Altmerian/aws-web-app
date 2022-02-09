package com.epam.cloudx.pavelsh.awsapp.sns_sqs.controller;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lambda")
@RequiredArgsConstructor
@Slf4j
public class LambdaController {

  private final AWSLambda awsLambda;

  @Value("${lambda.function-name}")
  private String functionARN;

  @PutMapping("/action")
  public ResponseEntity<Object> lambdaAction() {

    try {
      var invokeRequest = new InvokeRequest()
              .withFunctionName(functionARN)
              .withPayload("{\"detail-type\": \"AWP application\"}");
      awsLambda.invoke(invokeRequest);
    } catch (Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
