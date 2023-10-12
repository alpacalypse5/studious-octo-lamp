package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;

public interface ProfileEventStrategy {
    ValidationResult getValidationResult(
            ProfileEvent profileEvent, ProfileValidationRequest profileValidationRequest);
}
