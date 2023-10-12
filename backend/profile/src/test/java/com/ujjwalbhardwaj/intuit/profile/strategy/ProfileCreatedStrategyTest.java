package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfileCreatedStrategyTest {

    private ProfileCreatedStrategy profileCreatedStrategy;

    @BeforeEach
    public void setUp() {
        profileCreatedStrategy = new ProfileCreatedStrategy();
    }

    @Test
    public void whenGetValidationStatusCalled_thenAlwaysReturnInProgress() {
        ProfileEvent mockEvent = new ProfileEvent();
        ProfileValidationRequest mockRequest = new ProfileValidationRequest();

        ValidationResult status = profileCreatedStrategy.getValidationResult(mockEvent, mockRequest);

        assertEquals(ValidationStatus.IN_PROGRESS, status.getStatus());
    }

    @Test
    public void whenGetValidationStatusCalled_thenReturnRejectedOnRejectionReasons() {
        ProfileEvent mockEvent = new ProfileEvent();
        ProfileValidationRequest mockRequest = ProfileValidationRequest.builder()
                .rejectionReasons(List.of("rejection reason"))
                .build();

        ValidationResult status = profileCreatedStrategy.getValidationResult(mockEvent, mockRequest);

        assertEquals(ValidationStatus.REJECTED, status.getStatus());
    }
}
