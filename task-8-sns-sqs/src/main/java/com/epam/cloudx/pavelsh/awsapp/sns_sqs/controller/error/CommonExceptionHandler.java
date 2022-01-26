package com.epam.cloudx.pavelsh.awsapp.sns_sqs.controller.error;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

import com.epam.cloudx.pavelsh.awsapp.sns_sqs.dto.error.ErrorResponseDto;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandler {

  private final ErrorAttributes errorAttributes;

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleError(HttpServletRequest request) {
    ServletWebRequest servletWebRequest = new ServletWebRequest(request);
    Throwable error = errorAttributes.getError(servletWebRequest);
    var message = getErrorMessage(error);
    var errorResponse = ErrorResponseDto.builder().message(message).build();
    return ResponseEntity.status(SC_BAD_REQUEST).body(errorResponse);
  }

  private String getErrorMessage(Throwable e) {
    String message = null;
    if (e != null) {
      message = e.getMessage();
      message =
          !StringUtils.hasLength(message)
                  && e.getCause() != null
                  && !StringUtils.hasLength(e.getCause().getMessage())
              ? e.getCause().getMessage()
              : message;
    }
    return message;
  }
}
