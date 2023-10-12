package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileUpdatedStrategyTest {

    private ProfileUpdatedStrategy profileUpdatedStrategy;

    @BeforeEach
    public void setUp() {
        profileUpdatedStrategy = new ProfileUpdatedStrategy();
    }

    @Test
    public void whenGetValidationStatusCalled_thenAlwaysReturnInProgress() {
        ProfileEvent mockEvent = new ProfileEvent();
        ProfileValidationRequest mockRequest = new ProfileValidationRequest();

        ValidationResult status = profileUpdatedStrategy.getValidationResult(mockEvent, mockRequest);

        assertEquals(ValidationStatus.IN_PROGRESS, status.getStatus());
    }

}
