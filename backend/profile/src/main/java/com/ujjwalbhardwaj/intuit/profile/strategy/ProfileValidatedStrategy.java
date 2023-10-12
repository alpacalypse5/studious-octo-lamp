package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.enums.ProfileEventStatus;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class ProfileValidatedStrategy implements ProfileEventStrategy {

    @Override
    public ValidationResult getValidationResult(
            final ProfileEvent profileEvent, final ProfileValidationRequest profileValidationRequest) {
        final ValidationResult validationResult =  ValidationResult.builder()
                .event(profileValidationRequest.getEventType())
                .status(ValidationStatus.NO_STATUS)
                .build();
        if (ProfileEventStatus.VALIDATED_STATUSES.contains(profileEvent.getStatus())) {
            validationResult.setStatus(ValidationStatus.ACCEPTED);
        } else if (ProfileEventStatus.UPDATE_REJECTED.equals(profileEvent.getStatus())) {
            validationResult.setStatus(ValidationStatus.REJECTED);
            validationResult.setErrors(profileValidationRequest.getRejectionReasons());
        }

        return validationResult;
    }
}
