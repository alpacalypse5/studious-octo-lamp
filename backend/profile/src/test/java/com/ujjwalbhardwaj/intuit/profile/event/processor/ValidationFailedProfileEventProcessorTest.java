package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.model.ProfileDetails;
import com.ujjwalbhardwaj.intuit.profile.enums.ProfileEventStatus;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ValidationFailedProfileEventProcessorTest {

    @InjectMocks
    private ValidationFailedProfileEventProcessor validationFailedProfileEventProcessor;

    @Mock
    private ProfileEventRepository profileEventRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<ProfileEvent> profileEventCaptor;

    private ProfileEventPayload testPayload;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testPayload = new ProfileEventPayload();

        ProfileDetails profileDetails = new ProfileDetails();

        when(objectMapper.convertValue(any(), eq(ProfileDetails.class))).thenReturn(profileDetails);
    }

    @Test
    public void testProcessEvent() {
        validationFailedProfileEventProcessor.processEvent(testPayload);

        verify(profileEventRepository).save(profileEventCaptor.capture());
        ProfileEvent savedProfileEvent = profileEventCaptor.getValue();
        assertEquals(testPayload.getBusinessId(), savedProfileEvent.getBusinessId());
        assertEquals(EventType.PROFILE_VALIDATED, savedProfileEvent.getEventType());
        assertEquals(ProfileEventStatus.UPDATE_REJECTED, savedProfileEvent.getStatus());

        verify(objectMapper).convertValue(eq(testPayload.getProfileDetails()), eq(ProfileDetails.class));
    }

}

