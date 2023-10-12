package com.ujjwalbhardwaj.intuit.profile.exception;

public class BusinessProfileAlreadyExistsException extends ResourceExistsException {
    private static final long serialVersionUID = 1L;

    public BusinessProfileAlreadyExistsException(String businessId) {
        super("Business profile with ID " + businessId + " already exists.");
    }

    public BusinessProfileAlreadyExistsException(String businessId, Throwable cause) {
        super("Business profile with ID " + businessId + " already exists.", cause);
    }
}
