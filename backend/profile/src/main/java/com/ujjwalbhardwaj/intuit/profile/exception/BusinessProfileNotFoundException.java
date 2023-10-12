package com.ujjwalbhardwaj.intuit.profile.exception;

public class BusinessProfileNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 1L;

    public BusinessProfileNotFoundException(String businessId) {
        super("Business profile with ID " + businessId + " not found.");
    }

    public BusinessProfileNotFoundException(String businessId, Throwable cause) {
        super("Business profile with ID " + businessId + " not found.", cause);
    }
}
