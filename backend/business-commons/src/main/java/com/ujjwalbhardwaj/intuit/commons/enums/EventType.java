package com.ujjwalbhardwaj.intuit.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    CREATE_PROFILE("create-profile-event"),
    UPDATE_PROFILE("update-profile-event"),
    PROFILE_VALIDATED("validate-profile-event"),
    PROFILE_VALIDATION_FAILED("validation-failed-profile-event");

    private final String event;
}
