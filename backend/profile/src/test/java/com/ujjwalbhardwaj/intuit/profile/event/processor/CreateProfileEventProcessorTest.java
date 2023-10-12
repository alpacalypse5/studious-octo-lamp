package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileDetails;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.client.KafkaClient;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.exception.BusinessProfileAlreadyExistsException;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileRepository;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;


public class CreateProfileEventProcessorTest {

    @InjectMocks
    private CreateProfileEventProcessor createProfileEventProcessor;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ProfileValidationRequestRepository profileValidationRequestRepository;
    @Mock
    private KafkaClient kafkaClient;

    @Captor
    private ArgumentCaptor<ProfileValidationRequest> profileValidationRequestArgumentCaptor;

    private ProfileEventPayload testPayload;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testPayload = ProfileEventPayload.builder()
                .profileDetails(ProfileDetails.builder()
                        .businessAddress(com.ujjwalbhardwaj.intuit.commons.model.Address.builder().build())
                        .legalAddress(com.ujjwalbhardwaj.intuit.commons.model.Address.builder().build())
                        .build())
                .requestId("requestId")
                .businessId("businessId")
                .build();
    }

    @Test
    void testProcessEvent() {
        createProfileEventProcessor.processEvent(testPayload);

        verify(profileValidationRequestRepository).save(profileValidationRequestArgumentCaptor.capture());

        ProfileValidationRequest capturedProfile = profileValidationRequestArgumentCaptor.getValue();

        assertEquals("requestId", capturedProfile.getId());
    }

    @Test
    void testProcessEvent_withExistingBusiness() {
        Mockito.doReturn(true).when(profileRepository).existsById("businessId");
        assertThrows(BusinessProfileAlreadyExistsException.class, () -> createProfileEventProcessor.processEvent(testPayload));
        Mockito.verify(profileRepository).existsById("businessId");
    }

}

