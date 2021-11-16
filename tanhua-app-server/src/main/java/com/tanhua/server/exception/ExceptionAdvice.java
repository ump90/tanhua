package com.tanhua.server.exception;

import com.tanhua.pojo.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author UMP90
 * @date 2021/11/3
 */
@ControllerAdvice
public class ExceptionAdvice {
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<Object> businessExceptionAdvice(BusinessException businessException) {
    businessException.printStackTrace();
    ErrorResult errorResult = businessException.getErrorResult();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResult.getErrMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> otherExceptionAdvice(Exception exception) {
    exception.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResult.error());
  }
}
