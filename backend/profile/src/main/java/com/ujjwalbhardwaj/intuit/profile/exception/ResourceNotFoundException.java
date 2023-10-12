package com.ujjwalbhardwaj.intuit.profile.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String field;

    public ResourceNotFoundException(String message) {
        super(message);
        this.field = null;
    }

    public ResourceNotFoundException(String message, String field) {
        super(message);
        this.field = field;
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
    }

    public ResourceNotFoundException(String message, String field, Throwable cause) {
        super(message, cause);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
