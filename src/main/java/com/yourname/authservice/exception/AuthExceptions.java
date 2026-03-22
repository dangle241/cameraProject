package com.yourname.authservice.exception;

public class AuthExceptions {
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() { super("Invalid username or password"); }
    }

    public static class UserDisabledException extends RuntimeException {
        public UserDisabledException() { super("User is disabled"); }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException() { super("Invalid username or password"); }
    }
}
