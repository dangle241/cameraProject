package com.yourname.authservice.controller;

import com.yourname.authservice.exception.AuthExceptions.InvalidPasswordException;
import com.yourname.authservice.exception.AuthExceptions.UserDisabledException;
import com.yourname.authservice.exception.AuthExceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tai khoan");
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPassword() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai mat khau");
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<String> handleUserDisabled() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is disabled");
    }
}
