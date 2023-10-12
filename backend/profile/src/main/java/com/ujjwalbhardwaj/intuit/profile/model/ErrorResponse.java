package com.ujjwalbhardwaj.intuit.profile.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {
    private final String message;
}
