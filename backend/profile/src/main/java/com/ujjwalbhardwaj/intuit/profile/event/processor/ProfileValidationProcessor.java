package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import com.ujjwalbhardwaj.intuit.commons.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileValidationEventPayload;
import com.ujjwalbhardwaj.intuit.profile.client.KafkaClient;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileValidationRequest;
import com.ujjwalbhardwaj.intuit.profile.repository.ProfileValidationRequestRepository;
import com.ujjwalbhardwaj.intuit.profile.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileValidationProcessor {
    private final ProfileValidationRequestRepository profileValidationRequestRepository;
    private final KafkaClient kafkaClient;

    public void processValidationRequest(final ProfileValidationEventPayload profileValidationEventPayload) {
        final ProfileValidationRequest profileValidationRequest =
                getProfileValidationRequest(profileValidationEventPayload.getRequestId());
        final Set<Product> approvedProducts = CommonUtils.getOrEmptySet(profileValidationRequest.getApprovals());
        final Set<Product> rejectedProducts = CommonUtils.getOrEmptySet(profileValidationRequest.getRejections());

        if (ValidationStatus.SUCCESS.equals(profileValidationEventPayload.getStatus())) {
            if (!rejectedProducts.isEmpty() || CollectionUtils.isNotEmpty(profileValidationRequest.getRejectionReasons())) {
                log.info("Profile validation already rejected.");
                return;
            }
            approvedProducts.add(profileValidationEventPayload.getProductId());
            profileValidationRequest.setApprovals(approvedProducts);
        } else {
            rejectedProducts.add(profileValidationEventPayload.getProductId());
            profileValidationRequest.setRejections(rejectedProducts);

            List<String> rejectionReasons = CommonUtils.getOrEmptyList(profileValidationRequest.getRejectionReasons());
            rejectionReasons.add(profileValidationEventPayload.getMessage());
            profileValidationRequest.setRejectionReasons(rejectionReasons);
        }

        profileValidationRequestRepository.save(profileValidationRequest);
        sendKafkaEvents(rejectedProducts, approvedProducts, profileValidationEventPayload, profileValidationRequest);
    }

    private ProfileValidationRequest getProfileValidationRequest(final String requestId) {
        return profileValidationRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Profile validation request not found for id: " + requestId));
    }

    private void sendKafkaEvents(final Set<Product> rejectedProducts, final Set<Product> approvedProducts,
            final ProfileValidationEventPayload request, final ProfileValidationRequest profileValidationRequest) {
        if (!rejectedProducts.isEmpty() &&
                !Collections.disjoint(rejectedProducts, profileValidationRequest.getSubscribedProducts())) {
            kafkaClient.request("business-profile-event", buildProfileEventPayload(
                            EventType.PROFILE_VALIDATION_FAILED, request, profileValidationRequest),
                    profileValidationRequest.getBusinessId());
            return;
        }

        if (approvedProducts.containsAll(profileValidationRequest.getSubscribedProducts())) {
            kafkaClient.request("business-profile-event", buildProfileEventPayload(
                            EventType.PROFILE_VALIDATED, request, profileValidationRequest),
                    profileValidationRequest.getBusinessId());
        }
    }

    private ProfileEventPayload buildProfileEventPayload(final EventType eventType,
            final ProfileValidationEventPayload request, final ProfileValidationRequest profileValidationRequest) {
        return ProfileEventPayload.builder()
                .eventType(eventType)
                .profileDetails(request.getProfileDetails())
                .requestId(profileValidationRequest.getId())
                .businessId(profileValidationRequest.getBusinessId())
                .build();
    }

}
