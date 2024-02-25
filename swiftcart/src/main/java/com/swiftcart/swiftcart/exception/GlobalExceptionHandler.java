package com.swiftcart.swiftcart.exception;

import com.swiftcart.swiftcart.reponse.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception) {
        ErrorResponse response = new ErrorResponse(exception.getLocalizedMessage(), LocalDateTime.now(), 404);
        if (exception instanceof AccessDeniedException) {
            response.setStatusCode(403);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        if (exception instanceof AuthenticationException || exception instanceof BadCredentialsException) {
            response.setStatusCode(401);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
