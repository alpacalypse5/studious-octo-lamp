package com.ujjwalbhardwaj.intuit.profile.event;


import com.ujjwalbhardwaj.intuit.profile.event.processor.EventProcessor;

public interface ProfileEventFactory {

    EventProcessor getEventProcessor(String eventType);
}

