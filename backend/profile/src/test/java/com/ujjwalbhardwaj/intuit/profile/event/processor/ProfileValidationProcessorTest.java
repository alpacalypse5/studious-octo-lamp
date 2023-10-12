package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import com.ujjwalbhardwaj.intuit.commons.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileDetails;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileValidationEventPayload;
import com.ujjwalbhardwaj.intuit.profile.client.KafkaClient;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfileValidationProcessorTest {

    @InjectMocks
    private ProfileValidationProcessor profileValidationProcessor;

    @Mock
    private ProfileValidationRequestRepository profileValidationRequestRepository;

    @Mock
    private KafkaClient kafkaClient;

    @Captor
    private ArgumentCaptor<ProfileValidationRequest> profileValidationRequestCaptor;

    @Captor
    private ArgumentCaptor<String> kafkaTopicCaptor;

    @Captor
    private ArgumentCaptor<ProfileEventPayload> kafkaPayloadCaptor;

    @Captor
    private ArgumentCaptor<String> kafkaKeyCaptor;

    private ProfileValidationEventPayload testPayload;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testPayload = ProfileValidationEventPayload.builder()
                .requestId("request-id")
                .status(ValidationStatus.SUCCESS)
                .productId(Product.PAYROLL)
                .profileDetails(ProfileDetails.builder().build())
                .build();
    }

    @Test
    public void testProcessValidationRequestSuccessStatus() {
        ProfileValidationRequest mockProfileValidationRequest = ProfileValidationRequest.builder()
                .businessId("business-id")
                .rejections(new HashSet<>())
                .subscribedProducts(new HashSet<>())
                .approvals(new HashSet<>())
                .rejectionReasons(new ArrayList<>())
                .build();
        when(profileValidationRequestRepository.findById(anyString())).thenReturn(Optional.of(mockProfileValidationRequest));

        testPayload.setStatus(ValidationStatus.SUCCESS);

        profileValidationProcessor.processValidationRequest(testPayload);
        verify(profileValidationRequestRepository).save(profileValidationRequestCaptor.capture());
        ProfileValidationRequest savedProfileValidationRequest = profileValidationRequestCaptor.getValue();
        assertTrue(savedProfileValidationRequest.getApprovals().contains(testPayload.getProductId()));
    }

    @Test
    public void testProcessValidationRequest_whenAlreadyRejected() {
        ProfileValidationRequest mockProfileValidationRequest = ProfileValidationRequest.builder()
                .businessId("business-id")
                .rejections(Set.of(Product.ACCOUNTING))
                .subscribedProducts(new HashSet<>())
                .approvals(new HashSet<>())
                .rejectionReasons(new ArrayList<>())
                .build();
        when(profileValidationRequestRepository.findById(anyString())).thenReturn(Optional.of(mockProfileValidationRequest));

        testPayload.setStatus(ValidationStatus.SUCCESS);

        Assertions.assertDoesNotThrow(() -> profileValidationProcessor.processValidationRequest(testPayload));

        Mockito.verify(profileValidationRequestRepository).findById(anyString());
    }

    @Test
    public void testProcessValidationRequest_whenNewRejected() {
        ProfileValidationRequest mockProfileValidationRequest = ProfileValidationRequest.builder()
                .businessId("business-id")
                .rejections(new HashSet<>())
                .subscribedProducts(Set.of(Product.ACCOUNTING))
                .approvals(new HashSet<>())
                .rejectionReasons(new ArrayList<>())
                .build();
        when(profileValidationRequestRepository.findById(anyString())).thenReturn(Optional.of(mockProfileValidationRequest));

        testPayload.setStatus(ValidationStatus.FAILED);
        testPayload.setMessage("rejection reason");
        testPayload.setProductId(Product.ACCOUNTING);

        Assertions.assertDoesNotThrow(() -> profileValidationProcessor.processValidationRequest(testPayload));

        Mockito.verify(profileValidationRequestRepository).findById(anyString());
    }


    @Test
    public void testProcessValidationRequestOtherStatus() {
        ProfileValidationRequest mockProfileValidationRequest = ProfileValidationRequest.builder()
                .businessId("business-id")
                .rejections(new HashSet<>())
                .subscribedProducts(new HashSet<>())
                .approvals(new HashSet<>())
                .rejectionReasons(new ArrayList<>())
                .build();
        when(profileValidationRequestRepository.findById(anyString())).thenReturn(Optional.of(mockProfileValidationRequest));

        testPayload.setStatus(ValidationStatus.FAILED);

        profileValidationProcessor.processValidationRequest(testPayload);

        verify(profileValidationRequestRepository).save(profileValidationRequestCaptor.capture());

        ProfileValidationRequest savedProfileValidationRequest = profileValidationRequestCaptor.getValue();

        assertTrue(savedProfileValidationRequest.getRejections().contains(testPayload.getProductId()));
        assertTrue(savedProfileValidationRequest.getRejectionReasons().contains(testPayload.getMessage()));
    }

}

