package com.ujjwalbhardwaj.intuit.profile.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.client.KafkaClient;
import com.ujjwalbhardwaj.intuit.profile.client.SubscriptionServiceClient;
import com.ujjwalbhardwaj.intuit.profile.entity.Profile;
import com.ujjwalbhardwaj.intuit.profile.entity.ProfileEvent;
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
import com.ujjwalbhardwaj.intuit.profile.service.ProfileService;
import com.ujjwalbhardwaj.intuit.profile.strategy.ProfileEventContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileCacheRepository profileCacheRepository;
    private final ProfileEventRepository profileEventRepository;
    private final KafkaClient kafkaClient;
    private final SubscriptionServiceClient subscriptionServiceClient;
    private final ObjectMapper objectMapper;
    private final ProfileEventContext profileEventContext;
    private final ProfileValidationRequestRepository profileValidationRequestRepository;

    @Override
    public GetBusinessProfileResponse getBusinessProfile(final String businessId) {
        final Profile profile = profileCacheRepository.get(businessId);
        if (Objects.isNull(profile)) {
            throw new BusinessProfileNotFoundException(businessId);
        }

        return GetBusinessProfileResponse.builder()
                .id(profile.getId())
                .companyName(profile.getCompanyName())
                .legalName(profile.getLegalName())
                .businessAddress(profile.getBusinessAddress())
                .legalAddress(profile.getLegalAddress())
                .email(profile.getEmail())
                .website(profile.getWebsite())
                .build();
    }

    @Override
    public void createEvent(@NonNull final ProfileEventRequest eventRequest, final String businessId) throws JsonProcessingException {
        if (Objects.isNull(eventRequest.getPayload())) {
            log.error("Profile Details not found in the event {} for business {}", eventRequest, businessId);
            throw new InvalidRequestException("Profile Details not found!");
        }

        final ProfileEvent profileEvent = ProfileEvent.builder()
                .businessId(businessId)
                .eventType(eventRequest.getEventType())
                .payload(objectMapper.convertValue(eventRequest.getPayload(), ProfileDetails.class))
                .source(eventRequest.getSource())
                .build();
        profileEventRepository.save(profileEvent);
        kafkaClient.request("business-profile-event", buildProfileEventPayload(businessId, eventRequest), businessId);
    }

    @Override
    public ValidationResult getUpdateStatus(final String businessId) {
        final ProfileEvent profileEvent = profileEventRepository.findByBusinessId(businessId)
                .stream()
                .max(Comparator.comparing(ProfileEvent::getCreatedAt))
                .orElse(null);
        if (Objects.isNull(profileEvent)) {
            return ValidationResult.builder()
                    .status(ValidationStatus.NO_STATUS)
                    .build();
        }

        return profileEventContext.executeStrategy(profileEvent);
    }

    private ProfileEventPayload buildProfileEventPayload(final String businessId, final ProfileEventRequest profileEventRequest) throws JsonProcessingException {
        return ProfileEventPayload.builder()
                .requestId(UUID.randomUUID().toString())
                .businessId(businessId)
                .profileDetails(profileEventRequest.getPayload())
                .subscribedProducts(subscriptionServiceClient.getSubscribedProducts(businessId))
                .eventType(profileEventRequest.getEventType())
                .build();
    }

}
