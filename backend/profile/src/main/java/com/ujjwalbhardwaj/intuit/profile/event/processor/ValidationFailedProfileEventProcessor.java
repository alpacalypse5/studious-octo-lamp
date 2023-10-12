package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.model.ProfileDetails;
import com.ujjwalbhardwaj.intuit.profile.enums.ProfileEventStatus;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("validation-failed-profile-event")
@RequiredArgsConstructor
public class ValidationFailedProfileEventProcessor implements EventProcessor {
    private final ProfileEventRepository profileEventRepository;
    private final ObjectMapper objectMapper;


    public void processEvent(final ProfileEventPayload payload) {
        profileEventRepository.save(ProfileEvent.builder()
                .businessId(payload.getBusinessId())
                .eventType(EventType.PROFILE_VALIDATED)
                .payload(objectMapper.convertValue(payload.getProfileDetails(), ProfileDetails.class))
                .status(ProfileEventStatus.UPDATE_REJECTED)
                .build());
    }
}
