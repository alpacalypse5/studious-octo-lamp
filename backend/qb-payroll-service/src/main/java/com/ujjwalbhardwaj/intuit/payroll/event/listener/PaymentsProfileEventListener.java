package com.ujjwalbhardwaj.intuit.payroll.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import com.ujjwalbhardwaj.intuit.commons.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileValidationEventPayload;
import com.ujjwalbhardwaj.intuit.payroll.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentsProfileEventListener {
    private final BusinessService businessService;
    private final KafkaTemplate<String, ProfileValidationEventPayload> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "business-profile-validator", groupId = "payments-profile-consumer-group")
    public void listen(@Payload String message) throws JsonProcessingException {
        ProfileEventPayload payload = objectMapper.readValue(message, ProfileEventPayload.class);
        businessService.validate();
        kafkaTemplate.send("profile-validation-event", ProfileValidationEventPayload.builder()
                .requestId(payload.getRequestId())
                .message("validation success")
                .productId(Product.PAYMENTS)
                .status(ValidationStatus.SUCCESS)
                .profileDetails(payload.getProfileDetails())
                .build());
    }

}
