package com.socialmedia.SocialMediaApp.exceptions;

import com.socialmedia.SocialMediaApp.httpEntities.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setSuccess(false);
        errorResponse.setMessage("Resource "+ex.getResourceName() + " with field " + ex.getFieldName() + " not found");
        errorResponse.setErrorCode("404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
