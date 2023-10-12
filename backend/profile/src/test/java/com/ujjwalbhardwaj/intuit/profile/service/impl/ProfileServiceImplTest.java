package com.ujjwalbhardwaj.intuit.profile.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import com.ujjwalbhardwaj.intuit.profile.client.KafkaClient;
import com.ujjwalbhardwaj.intuit.profile.client.SubscriptionServiceClient;
import com.ujjwalbhardwaj.intuit.profile.entity.Profile;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.entity.model.ProfileDetails;
import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.exception.BusinessProfileNotFoundException;
import com.ujjwalbhardwaj.intuit.profile.exception.InvalidRequestException;
import com.ujjwalbhardwaj.intuit.profile.model.GetBusinessProfileResponse;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import com.ujjwalbhardwaj.intuit.profile.model.ProfileEventRequest;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileEventRepository;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import com.ujjwalbhardwaj.intuit.profile.repository.cache.ProfileCacheRepository;
import com.ujjwalbhardwaj.intuit.profile.strategy.ProfileEventContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

class ProfileServiceImplTest {

    @Mock
    private ProfileCacheRepository profileCacheRepository;
    @Mock
    private ProfileEventRepository profileEventRepository;
    @Mock
    private KafkaClient kafkaClient;
    @Mock
    private SubscriptionServiceClient subscriptionServiceClient;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ProfileEventContext profileEventContext;
    @Mock
    private ProfileValidationRequestRepository profileValidationRequestRepository;
    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBusinessProfile() {
        final String businessId = "testBusinessId";
        final Profile mockedProfile = Profile.builder()
                .id(businessId)
                .companyName("company-name")
                .build();
        Mockito.when(profileCacheRepository.get(businessId)).thenReturn(mockedProfile);

        final GetBusinessProfileResponse response = profileService.getBusinessProfile(businessId);

        Assertions.assertEquals(businessId, response.getId());

        Mockito.verify(profileCacheRepository).get(businessId);
    }

    @Test
    void testGetBusinessProfileWithBusinessProfileNotFound() {
        final String businessId = "testBusinessId";
        Mockito.when(profileCacheRepository.get(businessId)).thenReturn(null);

        Assertions.assertThrows(BusinessProfileNotFoundException.class, () -> profileService.getBusinessProfile(businessId));

        Mockito.verify(profileCacheRepository).get(businessId);
    }

    @Test
    void testCreateEvent() {
        final ProfileEventRequest eventRequest = ProfileEventRequest.builder()
                .eventType(EventType.CREATE_PROFILE)
                .source(Product.PAYROLL)
                .payload(com.ujjwalbhardwaj.intuit.commons.model.ProfileDetails.builder()
                        .companyName("company-name")
                        .build())
                .build();
        final String businessId = "testBusinessId";
        Mockito.when(objectMapper.convertValue(any(), eq(ProfileDetails.class))).thenReturn(ProfileDetails.builder()
                .companyName("company-name")
                .build());

        Assertions.assertDoesNotThrow(() -> profileService.createEvent(eventRequest, businessId));

        final ArgumentCaptor<ProfileEvent> captor = ArgumentCaptor.forClass(ProfileEvent.class);
        Mockito.verify(profileEventRepository).save(captor.capture());
        Mockito.verify(objectMapper).convertValue(any(), eq(ProfileDetails.class));
    }

    @Test
    void testCreateEventWithInvalidRequest() {
        final ProfileEventRequest eventRequest = ProfileEventRequest.builder()
                .eventType(EventType.CREATE_PROFILE)
                .source(Product.PAYROLL)
                .payload(null)
                .build();
        final String businessId = "testBusinessId";
        Assertions.assertThrows(InvalidRequestException.class, () -> profileService.createEvent(eventRequest, businessId));
    }

    @Test
    public void testGetUpdateStatus_NoStatus() {
        when(profileEventRepository.findByBusinessId(anyString())).thenReturn(Collections.emptyList());

        ValidationResult response = profileService.getUpdateStatus("someBusinessId");

        Assertions.assertEquals(ValidationStatus.NO_STATUS, response.getStatus());
        verify(profileEventRepository).findByBusinessId("someBusinessId");
    }

    @Test
    public void testGetUpdateStatus_RejectedStatus() {
        ProfileEvent mockEvent = mock(ProfileEvent.class);
        when(mockEvent.getCreatedAt()).thenReturn(new Date());
        when(profileEventRepository.findByBusinessId(anyString())).thenReturn(Collections.singletonList(mockEvent));
        when(profileEventContext.executeStrategy(mockEvent)).thenReturn(ValidationResult.builder()
                        .status(ValidationStatus.REJECTED)
                        .errors(List.of("Some error"))
                .build());

        ProfileValidationRequest mockRequest = mock(ProfileValidationRequest.class);
        when(mockRequest.getRejectionReasons()).thenReturn(Collections.singletonList("Some error"));
        when(profileValidationRequestRepository.findByBusinessId(anyString())).thenReturn(Collections.singletonList(mockRequest));

        ValidationResult response = profileService.getUpdateStatus("someBusinessId");

        Assertions.assertEquals(ValidationStatus.REJECTED, response.getStatus());
        Assertions.assertEquals("Some error", response.getErrors().get(0));
    }

    @Test
    public void testGetUpdateStatus_AnotherStatus() {
        ProfileEvent mockEvent = mock(ProfileEvent.class);
        when(mockEvent.getCreatedAt()).thenReturn(new Date());
        when(profileEventRepository.findByBusinessId(anyString())).thenReturn(Collections.singletonList(mockEvent));
        when(profileEventContext.executeStrategy(mockEvent)).thenReturn(ValidationResult.builder()
                .status(ValidationStatus.IN_PROGRESS)
                .build());  // or any other status

        ValidationResult response = profileService.getUpdateStatus("someBusinessId");

        Assertions.assertEquals(ValidationStatus.IN_PROGRESS, response.getStatus());
        Assertions.assertNull(response.getErrors());  // Assuming errors are null when not REJECTED
    }

    @Test
    public void testGetUpdateStatus_LatestEventSelected() {
        ProfileEvent oldEvent = mock(ProfileEvent.class);
        when(oldEvent.getCreatedAt()).thenReturn(
                Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant()));

        ProfileEvent latestEvent = mock(ProfileEvent.class);
        when(latestEvent.getCreatedAt()).thenReturn(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        when(profileEventRepository.findByBusinessId(anyString())).thenReturn(Arrays.asList(oldEvent, latestEvent));
        when(profileEventContext.executeStrategy(latestEvent)).thenReturn(ValidationResult.builder()
                .status(ValidationStatus.IN_PROGRESS)
                .build());

        ValidationResult response = profileService.getUpdateStatus("someBusinessId");

        Assertions.assertEquals(ValidationStatus.IN_PROGRESS, response.getStatus());
        verify(profileEventContext).executeStrategy(latestEvent);
        verify(profileEventContext, never()).executeStrategy(oldEvent);
    }
}

