package com.ujjwalbhardwaj.intuit.profile.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileValidationEventPayload;
import com.ujjwalbhardwaj.intuit.profile.event.processor.ProfileValidationProcessor;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class ProfileValidationListenerTest {

    @InjectMocks
    private ProfileValidationListener profileValidationListener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ProfileValidationProcessor profileValidationProcessor;

    @Mock
    private ProfileValidationRequestRepository profileValidationRequestRepository;

    @Captor
    private ArgumentCaptor<ProfileValidationEventPayload> payloadCaptor;

    private final String testMessage = "{\"someKey\": \"someValue\"}";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListen_success() throws Exception {
        ProfileValidationEventPayload samplePayload = new ProfileValidationEventPayload();

        when(objectMapper.readValue(testMessage, ProfileValidationEventPayload.class)).thenReturn(samplePayload);

        Assertions.assertDoesNotThrow(() -> profileValidationListener.listen(testMessage));

        verify(objectMapper).readValue(testMessage, ProfileValidationEventPayload.class);
        verify(profileValidationProcessor).processValidationRequest(payloadCaptor.capture());
    }

    @Test
    public void testListen_failure() throws Exception {
        when(objectMapper.readValue(testMessage, ProfileValidationEventPayload.class)).thenThrow(new RuntimeException("Mock Exception"));

        Assertions.assertDoesNotThrow(() -> profileValidationListener.listen(testMessage));

        verify(objectMapper).readValue(testMessage, ProfileValidationEventPayload.class);
        verify(profileValidationProcessor, times(0)).processValidationRequest(any());
    }
}

