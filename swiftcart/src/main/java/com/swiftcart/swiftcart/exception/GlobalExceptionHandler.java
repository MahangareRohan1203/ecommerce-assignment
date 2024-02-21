package com.swiftcart.swiftcart.exception;

import com.swiftcart.swiftcart.reponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.naming.AuthenticationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception) {
        ErrorResponse response = new ErrorResponse(exception.getLocalizedMessage(), LocalDateTime.now(), 404);
        if (exception instanceof AuthenticationException)
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
