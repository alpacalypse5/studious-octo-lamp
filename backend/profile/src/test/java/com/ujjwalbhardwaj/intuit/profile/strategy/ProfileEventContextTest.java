package com.ujjwalbhardwaj.intuit.profile.strategy;

import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProfileEventContextTest {

    @InjectMocks
    private ProfileEventContext profileEventContext;

    @Mock
    private ProfileEventStrategy mockStrategy;

    @Mock
    private ProfileValidationRequestRepository profileValidationRequestRepository;

    @Captor
    private ArgumentCaptor<ProfileEvent> profileEventArgumentCaptor;
    @Captor
    private ArgumentCaptor<ProfileValidationRequest> profileValidationRequestArgumentCaptor;

    private Map<EventType, ProfileEventStrategy> eventTypeProfileEventStrategyMap;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        eventTypeProfileEventStrategyMap = new HashMap<>();
        eventTypeProfileEventStrategyMap.put(EventType.CREATE_PROFILE, mockStrategy);

        ReflectionTestUtils.setField(profileEventContext, "eventTypeProfileEventStrategyMap", eventTypeProfileEventStrategyMap);
    }

    @Test
    public void testExecuteStrategy_StrategyExists() {
        ProfileEvent mockEvent = mock(ProfileEvent.class);
        when(mockEvent.getEventType()).thenReturn(EventType.CREATE_PROFILE);
        when(mockEvent.getBusinessId()).thenReturn("business-id");
        when(profileValidationRequestRepository.findByBusinessId(anyString())).thenReturn(List.of(ProfileValidationRequest.builder()
                .id("request-id")
                .eventType(EventType.CREATE_PROFILE)
                .createdAt(new Date())
                .build()));
        when(mockStrategy.getValidationResult(any(ProfileEvent.class), any(ProfileValidationRequest.class))).thenReturn(ValidationResult.builder()
                        .status(ValidationStatus.IN_PROGRESS)
                .build());
        ValidationResult result = profileEventContext.executeStrategy(mockEvent);

        Assertions.assertEquals(ValidationStatus.IN_PROGRESS, result.getStatus());
        verify(mockStrategy).getValidationResult(profileEventArgumentCaptor.capture(), profileValidationRequestArgumentCaptor.capture());

        ProfileEvent capturedEvent = profileEventArgumentCaptor.getValue();
        Assertions.assertEquals(mockEvent, capturedEvent);
    }

    @Test
    public void testExecuteStrategy_StrategyDoesNotExist() {
        ProfileEvent mockEvent = mock(ProfileEvent.class);
        when(mockEvent.getEventType()).thenReturn(EventType.UPDATE_PROFILE);
        when(profileValidationRequestRepository.findByBusinessId(anyString())).thenReturn(List.of(ProfileValidationRequest.builder()
                        .id("request-id")
                        .eventType(EventType.UPDATE_PROFILE)
                .build()));

        ValidationResult result = profileEventContext.executeStrategy(mockEvent);

        Assertions.assertEquals(ValidationStatus.NO_STATUS, result.getStatus());
        verifyNoInteractions(mockStrategy);
    }
}
