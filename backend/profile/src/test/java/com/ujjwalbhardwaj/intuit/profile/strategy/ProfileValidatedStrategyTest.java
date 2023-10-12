package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.enums.ProfileEventStatus;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileValidatedStrategyTest {

    private ProfileValidatedStrategy profileValidatedStrategy;

    @BeforeEach
    public void setUp() {
        profileValidatedStrategy = new ProfileValidatedStrategy();
    }

    @Test
    public void whenUpdateAccepted_thenValidationStatusIsAccepted() {
        ProfileEvent mockEvent = new ProfileEvent();
        ProfileValidationRequest mockRequest = new ProfileValidationRequest();
        mockEvent.setStatus(ProfileEventStatus.UPDATE_ACCEPTED);

        ValidationResult result = profileValidatedStrategy.getValidationResult(mockEvent, mockRequest);

        assertEquals(ValidationStatus.ACCEPTED, result.getStatus());
    }

    @Test
    public void whenUpdatedRejected_thenValidationStatusIsRejected() {
        ProfileEvent mockEvent = new ProfileEvent();
        ProfileValidationRequest mockRequest = new ProfileValidationRequest();
        mockEvent.setStatus(ProfileEventStatus.UPDATE_REJECTED);

        ValidationResult result = profileValidatedStrategy.getValidationResult(mockEvent, mockRequest);

        assertEquals(ValidationStatus.REJECTED, result.getStatus());
    }

    @Test
    public void whenStatusIsNeitherAcceptedNorRejected_thenValidationStatusIsNoStatus() {
        ProfileEvent mockEvent = new ProfileEvent();
        ProfileValidationRequest mockRequest = new ProfileValidationRequest();
        mockEvent.setStatus(ProfileEventStatus.CREATED);

        ValidationResult result = profileValidatedStrategy.getValidationResult(mockEvent, mockRequest);

        assertEquals(ValidationStatus.NO_STATUS, result.getStatus());
    }
}
