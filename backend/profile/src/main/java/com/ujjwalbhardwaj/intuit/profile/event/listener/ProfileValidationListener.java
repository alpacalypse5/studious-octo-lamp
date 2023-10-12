package com.ujjwalbhardwaj.intuit.profile.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileValidationEventPayload;
import com.ujjwalbhardwaj.intuit.profile.event.processor.ProfileValidationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileValidationListener {
    private final ObjectMapper objectMapper;
    private final ProfileValidationProcessor profileValidationProcessor;

    @KafkaListener(topics = "${kafka.profile.validation.event.topic}", groupId = "${kafka.profile.consumer.group}")
    public void listen(@Payload String message) {
        log.info("Received message from profile validation event with payload: {}", message);

        try {
            final ProfileValidationEventPayload request = objectMapper.readValue(message, ProfileValidationEventPayload.class);
            profileValidationProcessor.processValidationRequest(request);
        } catch (final Exception e) {
            log.error("Error processing profile validation event: ", e);
        }
    }
}
