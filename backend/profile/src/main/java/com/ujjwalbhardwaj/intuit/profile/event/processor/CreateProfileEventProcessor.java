package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.client.KafkaClient;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.exception.BusinessProfileAlreadyExistsException;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileRepository;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("create-profile-event")
@RequiredArgsConstructor
public class CreateProfileEventProcessor implements EventProcessor {
    private final ProfileValidationRequestRepository profileValidationRequestRepository;
    private final ProfileRepository profileRepository;
    private final KafkaClient kafkaClient;

    public void processEvent(final ProfileEventPayload payload) {
        final ProfileValidationRequest profileValidationRequest = ProfileValidationRequest.builder()
                .id(payload.getRequestId())
                .businessId(payload.getBusinessId())
                .subscribedProducts(payload.getSubscribedProducts())
                .eventType(payload.getEventType())
                .build();
        if (profileRepository.existsById(payload.getBusinessId())) {
            profileValidationRequest.setRejectionReasons(List.of("Business already exists!"));
            profileValidationRequestRepository.save(profileValidationRequest);
            throw new BusinessProfileAlreadyExistsException(payload.getBusinessId());
        }

        profileValidationRequestRepository.save(profileValidationRequest);
        kafkaClient.request("business-profile-validator", payload, profileValidationRequest.getBusinessId());
    }
}
