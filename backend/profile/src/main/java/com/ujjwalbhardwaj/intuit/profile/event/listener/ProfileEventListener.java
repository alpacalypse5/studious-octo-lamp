package com.ujjwalbhardwaj.intuit.profile.event.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.profile.event.ProfileEventFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileEventListener {
    private final ProfileEventFactory profileEventFactory;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.profile.event.topic}", groupId = "${kafka.profile.consumer.group}")
    public void listen(@Payload final String message) {
        log.info("Received message from profile event with payload: {}", message);
        try {
            final ProfileEventPayload request = objectMapper.readValue(message, ProfileEventPayload.class);
            profileEventFactory.getEventProcessor(request.getEventType().getEvent())
                    .processEvent(request);
        } catch (final Exception e) {
            log.error("Error processing profile event: ", e);
        }
    }

}
