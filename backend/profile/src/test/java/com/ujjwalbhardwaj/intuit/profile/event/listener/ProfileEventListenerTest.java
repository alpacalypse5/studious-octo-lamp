package com.ujjwalbhardwaj.intuit.profile.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.event.ProfileEventFactory;
import com.ujjwalbhardwaj.intuit.profile.event.processor.EventProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProfileEventListenerTest {

    @InjectMocks
    private ProfileEventListener profileEventListener;

    @Mock
    private ProfileEventFactory profileEventFactory;

    @Mock
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<ProfileEventPayload> payloadCaptor;

    private final String testMessage = "{\"eventType\": {\"event\": \"SAMPLE_EVENT\"}}";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListen_success() throws Exception {
        ProfileEventPayload samplePayload = ProfileEventPayload.builder()
                .eventType(EventType.CREATE_PROFILE)
                .build();
        EventProcessor mockProcessor = mock(EventProcessor.class);

        when(objectMapper.readValue(testMessage, ProfileEventPayload.class)).thenReturn(samplePayload);
        when(profileEventFactory.getEventProcessor(any())).thenReturn(mockProcessor);

        Assertions.assertDoesNotThrow(() -> profileEventListener.listen(testMessage));

        verify(objectMapper).readValue(testMessage, ProfileEventPayload.class);
        verify(profileEventFactory).getEventProcessor(any());
        verify(mockProcessor).processEvent(payloadCaptor.capture());
    }

    @Test
    public void testListen_failure() throws Exception {
        when(objectMapper.readValue(testMessage, ProfileEventPayload.class)).thenThrow(new RuntimeException("Mock Exception"));

        Assertions.assertDoesNotThrow(() -> profileEventListener.listen(testMessage));

        verify(objectMapper).readValue(testMessage, ProfileEventPayload.class);
        verify(profileEventFactory, times(0)).getEventProcessor(any());
    }
}

