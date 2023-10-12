package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class ProfileCreatedStrategy implements ProfileEventStrategy {

    @Override
    public ValidationResult getValidationResult(
            final ProfileEvent profileEvent, final ProfileValidationRequest profileValidationRequest) {
        if (CollectionUtils.isNotEmpty(profileValidationRequest.getRejectionReasons())) {
            return ValidationResult.builder()
                    .status(ValidationStatus.REJECTED)
                    .event(profileValidationRequest.getEventType())
                    .errors(profileValidationRequest.getRejectionReasons())
                    .build();
        }

        return ValidationResult.builder()
                .status(ValidationStatus.IN_PROGRESS)
                .event(profileValidationRequest.getEventType())
                .build();
    }
}
