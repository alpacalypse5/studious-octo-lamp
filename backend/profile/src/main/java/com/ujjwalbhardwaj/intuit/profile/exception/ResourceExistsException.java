package com.ujjwalbhardwaj.intuit.profile.exception;

public class ResourceExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String field;

    public ResourceExistsException(String message) {
        super(message);
        this.field = null;
    }

    public ResourceExistsException(String message, String field) {
        super(message);
        this.field = field;
    }

    public ResourceExistsException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
    }

    public ResourceExistsException(String message, String field, Throwable cause) {
        super(message, cause);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
