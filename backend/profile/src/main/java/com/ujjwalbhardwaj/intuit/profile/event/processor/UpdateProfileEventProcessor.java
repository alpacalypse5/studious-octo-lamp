package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.client.KafkaClient;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("update-profile-event")
@RequiredArgsConstructor
public class UpdateProfileEventProcessor implements EventProcessor {
    private final KafkaClient kafkaClient;
    private final ProfileValidationRequestRepository profileValidationRequestRepository;

    public void processEvent(final ProfileEventPayload payload) {
        final ProfileValidationRequest profileValidationRequest = ProfileValidationRequest.builder()
                .id(payload.getRequestId())
                .businessId(payload.getBusinessId())
                .subscribedProducts(payload.getSubscribedProducts())
                .eventType(EventType.UPDATE_PROFILE)
                .build();
        profileValidationRequestRepository.save(profileValidationRequest);
        kafkaClient.request("business-profile-validator", payload, profileValidationRequest.getBusinessId());
    }
}
