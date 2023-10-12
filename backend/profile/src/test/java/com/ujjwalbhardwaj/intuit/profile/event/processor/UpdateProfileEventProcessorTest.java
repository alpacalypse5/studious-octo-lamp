package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.client.KafkaClient;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


public class UpdateProfileEventProcessorTest {

    @InjectMocks
    private UpdateProfileEventProcessor updateProfileEventProcessor;

    @Mock
    private KafkaClient kafkaClient;

    @Mock
    private ProfileValidationRequestRepository profileValidationRequestRepository;

    @Captor
    private ArgumentCaptor<ProfileValidationRequest> profileValidationRequestCaptor;

    @Captor
    private ArgumentCaptor<String> kafkaTopicCaptor;

    @Captor
    private ArgumentCaptor<ProfileEventPayload> kafkaPayloadCaptor;

    @Captor
    private ArgumentCaptor<String> kafkaKeyCaptor;

    private ProfileEventPayload testPayload;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testPayload = new ProfileEventPayload();
    }

    @Test
    public void testProcessEvent() {
        updateProfileEventProcessor.processEvent(testPayload);

        verify(profileValidationRequestRepository).save(profileValidationRequestCaptor.capture());
        ProfileValidationRequest savedProfileValidationRequest = profileValidationRequestCaptor.getValue();
        assertEquals(testPayload.getRequestId(), savedProfileValidationRequest.getId());
        assertEquals(testPayload.getBusinessId(), savedProfileValidationRequest.getBusinessId());
        assertEquals(testPayload.getSubscribedProducts(), savedProfileValidationRequest.getSubscribedProducts());

        verify(kafkaClient).request(kafkaTopicCaptor.capture(), kafkaPayloadCaptor.capture(), kafkaKeyCaptor.capture());
        assertEquals("business-profile-validator", kafkaTopicCaptor.getValue());
        assertEquals(testPayload, kafkaPayloadCaptor.getValue());
        assertEquals(testPayload.getBusinessId(), kafkaKeyCaptor.getValue());
    }

}

