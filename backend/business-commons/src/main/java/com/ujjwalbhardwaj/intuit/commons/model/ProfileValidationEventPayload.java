package com.ujjwalbhardwaj.intuit.commons.model;

import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import com.ujjwalbhardwaj.intuit.commons.enums.ValidationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileValidationEventPayload {
    private String requestId;
    private Product productId;
    private ValidationStatus status;
    private String message;
    private ProfileDetails profileDetails;
}
