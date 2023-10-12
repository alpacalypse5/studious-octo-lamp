package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileDetails;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.entity.Profile;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.enums.ProfileEventStatus;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileEventRepository;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileRepository;
import com.ujjwalbhardwaj.intuit.profile.repository.cache.ProfileCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ValidateProfileEventProcessorTest {

    @InjectMocks
    private ValidateProfileEventProcessor validateProfileEventProcessor;

    @Mock
    private ProfileEventRepository profileEventRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileCacheRepository profileCacheRepository;

    @Captor
    private ArgumentCaptor<ProfileEvent> profileEventCaptor;

    @Captor
    private ArgumentCaptor<Profile> profileCaptor;

    @Captor
    private ArgumentCaptor<String> businessIdCaptor;

    private ProfileEventPayload testPayload;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testPayload = ProfileEventPayload.builder()
                .profileDetails(ProfileDetails.builder()
                        .businessAddress(com.ujjwalbhardwaj.intuit.commons.model.Address.builder().build())
                        .legalAddress(com.ujjwalbhardwaj.intuit.commons.model.Address.builder().build())
                        .build())
                .build();
    }

    @Test
    public void testProcessEvent() {
        when(profileRepository.findById(any())).thenReturn(Optional.empty());

        validateProfileEventProcessor.processEvent(testPayload);

        verify(profileEventRepository).save(profileEventCaptor.capture());
        ProfileEvent savedProfileEvent = profileEventCaptor.getValue();
        assertEquals(testPayload.getBusinessId(), savedProfileEvent.getBusinessId());
        assertEquals(EventType.PROFILE_VALIDATED, savedProfileEvent.getEventType());
        assertEquals(ProfileEventStatus.UPDATE_ACCEPTED, savedProfileEvent.getStatus());

        verify(profileRepository).save(profileCaptor.capture());
        Profile savedProfile = profileCaptor.getValue();
        assertEquals(testPayload.getBusinessId(), savedProfile.getId());
        assertEquals(testPayload.getProfileDetails().getCompanyName(), savedProfile.getCompanyName());

        verify(profileCacheRepository).put(businessIdCaptor.capture(), profileCaptor.capture());
        assertEquals(testPayload.getBusinessId(), businessIdCaptor.getValue());
        assertEquals(testPayload.getBusinessId(), profileCaptor.getValue().getId());
    }

    @Test
    public void testProcessEvent_withBusinessId() {
        when(profileRepository.findById(any())).thenReturn(Optional.empty());
        testPayload.setBusinessId("testBusinessId");

        validateProfileEventProcessor.processEvent(testPayload);

        verify(profileEventRepository).save(profileEventCaptor.capture());
        ProfileEvent savedProfileEvent = profileEventCaptor.getValue();
        assertEquals(testPayload.getBusinessId(), savedProfileEvent.getBusinessId());
        assertEquals(EventType.PROFILE_VALIDATED, savedProfileEvent.getEventType());
        assertEquals(ProfileEventStatus.UPDATE_ACCEPTED, savedProfileEvent.getStatus());

        verify(profileRepository).save(profileCaptor.capture());
        Profile savedProfile = profileCaptor.getValue();
        assertEquals(testPayload.getBusinessId(), savedProfile.getId());
        assertEquals(testPayload.getProfileDetails().getCompanyName(), savedProfile.getCompanyName());

        verify(profileCacheRepository).put(businessIdCaptor.capture(), profileCaptor.capture());
        assertEquals(testPayload.getBusinessId(), businessIdCaptor.getValue());
        assertEquals(testPayload.getBusinessId(), profileCaptor.getValue().getId());
    }
}

