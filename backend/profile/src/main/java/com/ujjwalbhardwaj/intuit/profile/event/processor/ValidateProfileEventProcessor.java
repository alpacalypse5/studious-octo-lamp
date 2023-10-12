package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.entity.Profile;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.model.Address;
import com.ujjwalbhardwaj.intuit.profile.entity.model.ProfileDetails;
import com.ujjwalbhardwaj.intuit.profile.enums.ProfileEventStatus;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileEventRepository;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileRepository;
import com.ujjwalbhardwaj.intuit.profile.repository.cache.ProfileCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("validate-profile-event")
@RequiredArgsConstructor
public class ValidateProfileEventProcessor implements EventProcessor {
    private final ProfileEventRepository profileEventRepository;
    private final ObjectMapper objectMapper;
    private final ProfileRepository profileRepository;
    private final ProfileCacheRepository profileCacheRepository;

    public void processEvent(final ProfileEventPayload payload) {
        profileEventRepository.save(ProfileEvent.builder()
                .businessId(payload.getBusinessId())
                .eventType(EventType.PROFILE_VALIDATED)
                .status(ProfileEventStatus.UPDATE_ACCEPTED)
                .payload(objectMapper.convertValue(payload.getProfileDetails(), ProfileDetails.class))
                .build());

        Profile profile = Profile.builder().build();
        if (payload.getBusinessId() != null) {
            profile = profileRepository.findById(payload.getBusinessId()).orElse(Profile.builder().build());
        }
        profile.setId(payload.getBusinessId());
        profile.setCompanyName(payload.getProfileDetails().getCompanyName());
        profile.setLegalName(payload.getProfileDetails().getLegalName());
        profile.setBusinessAddress(objectMapper.convertValue(payload.getProfileDetails().getBusinessAddress(), Address.class));
        profile.setLegalAddress(objectMapper.convertValue(payload.getProfileDetails().getLegalAddress(), Address.class));
        profile.setEmail(payload.getProfileDetails().getEmail());
        profile.setWebsite(payload.getProfileDetails().getWebsite());
        profileRepository.save(profile);
        profileCacheRepository.put(payload.getBusinessId(), profile);
    }
}
