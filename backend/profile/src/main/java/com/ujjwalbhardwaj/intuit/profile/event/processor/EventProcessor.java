package com.ujjwalbhardwaj.intuit.profile.event.processor;

import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventPayload;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventProcessor {
    void processEvent(ProfileEventPayload payload) throws JsonProcessingException;
}