package com.ayush.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class GlobalExceptionHandler
{
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(
            UserAlreadyExistsException ex
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        Map.of(
                                "timestamp", LocalDateTime.now(),
                                "message", ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> handleRoleNotFound(
            RoleNotFoundException ex
    ){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        Map.of("timestamp", LocalDateTime.now(),
                                "message", ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException ex
    ){
        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(
                        Map.of(
                                "timestamp", LocalDateTime.now(),
                                "message", message
                        )
                );
    }


}
