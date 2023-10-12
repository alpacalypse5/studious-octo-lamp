package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import com.ujjwalbhardwaj.intuit.profile.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProfileEventContext {
    private static final long TIMEOUT_IN_MINUTES = 1L;

    private final Map<EventType, ProfileEventStrategy> eventTypeProfileEventStrategyMap;
    private final ProfileValidationRequestRepository profileValidationRequestRepository;

    public ValidationResult executeStrategy(ProfileEvent profileEvent) {
        ProfileEventStrategy strategy = eventTypeProfileEventStrategyMap.get(profileEvent.getEventType());
        final ProfileValidationRequest profileValidationRequest =
                profileValidationRequestRepository.findByBusinessId(profileEvent.getBusinessId())
                        .stream()
                        .max(Comparator.comparing(ProfileValidationRequest::getCreatedAt))
                        .orElse(ProfileValidationRequest.builder().build());

        if (Objects.isNull(strategy)) {
            return ValidationResult.builder()
                    .status(ValidationStatus.NO_STATUS)
                    .event(profileValidationRequest.getEventType())
                    .build();
        }

        final ValidationResult validationResult = strategy.getValidationResult(profileEvent, profileValidationRequest);
        handleTimeout(validationResult, profileValidationRequest);

        return validationResult;
    }

    private void handleTimeout(
            final ValidationResult validationResult, final ProfileValidationRequest profileValidationRequest) {
        if (!ValidationStatus.IN_PROGRESS.equals(validationResult.getStatus())) {
            return;
        }

        if (!CommonUtils.isTimedOut(profileValidationRequest.getCreatedAt(), TIMEOUT_IN_MINUTES)) {
            return;
        }

        final List<String> errors = List.of("Update request timed out!");

        validationResult.setStatus(ValidationStatus.REJECTED);
        validationResult.setErrors(errors);
        profileValidationRequest.setRejectionReasons(errors);
        profileValidationRequestRepository.save(profileValidationRequest);
    }
}