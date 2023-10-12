package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class ProfileUpdatedStrategy implements ProfileEventStrategy {
    @Override
    public ValidationResult getValidationResult(
            final ProfileEvent profileEvent, final ProfileValidationRequest profileValidationRequest) {
        return ValidationResult.builder()
                .status(ValidationStatus.IN_PROGRESS)
                .event(profileValidationRequest.getEventType())
                .build();
    }
}
