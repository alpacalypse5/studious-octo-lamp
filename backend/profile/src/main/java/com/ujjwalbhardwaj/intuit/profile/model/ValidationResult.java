package com.ujjwalbhardwaj.intuit.profile.model;

import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Builder
@Data
public class ValidationResult {
    private final EventType event;
    @Setter
    private ValidationStatus status;
    @Setter
    private List<String> errors;

}
