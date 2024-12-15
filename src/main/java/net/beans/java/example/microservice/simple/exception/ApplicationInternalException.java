package net.beans.java.example.microservice.simple.exception;

public class ApplicationInternalException extends RuntimeException {

    public ApplicationInternalException(String message) {
        super(message);
    }

    public ApplicationInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
