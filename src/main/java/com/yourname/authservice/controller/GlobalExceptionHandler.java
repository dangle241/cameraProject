package com.yourname.authservice.controller;

import com.yourname.authservice.exception.AuthExceptions.InvalidPasswordException;
import com.yourname.authservice.exception.AuthExceptions.UserDisabledException;
import com.yourname.authservice.exception.AuthExceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String INVALID_CREDENTIALS = "Ten dang nhap hoac mat khau khong dung.";

    @ExceptionHandler({UserNotFoundException.class, InvalidPasswordException.class})
    public ResponseEntity<String> handleInvalidLogin() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(INVALID_CREDENTIALS);
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<String> handleUserDisabled() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tai khoan da bi khoa.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Du lieu khong hop le.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception ex) {
        log.error("Unhandled error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Loi he thong. Vui long thu lai sau.");
    }
}
