package com.ujjwalbhardwaj.intuit.profile.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaClient {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void request(final String topicName, final Object payload, final String messageKey) {
        log.info("Kafka request to topic: {} with messageKey: {} and payload: {}", topicName, messageKey, payload);
        try {
            kafkaTemplate.send(topicName, messageKey, objectMapper.writeValueAsString(payload));
        } catch (final Exception e) {
            log.info("Failed while pushing message to topic: {}", topicName, e);
        }
    }


}
