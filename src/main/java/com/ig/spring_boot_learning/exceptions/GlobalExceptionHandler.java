package com.ig.spring_boot_learning.exceptions;

import com.ig.spring_boot_learning.base.response.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApiErrorException.class})
    public ResponseEntity<Object> handleApiErrorException(ApiErrorException ex) {
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("response", new ResponseMessage(ex.getCode(), ex.getMessage()));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, HttpStatusCode status, @NonNull WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ResponseMessage response = new ResponseMessage(status.value(), status.toString());
        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("response", response);
        map.put("errors", errors);
        return new ResponseEntity<>(map, status);
    }
}
