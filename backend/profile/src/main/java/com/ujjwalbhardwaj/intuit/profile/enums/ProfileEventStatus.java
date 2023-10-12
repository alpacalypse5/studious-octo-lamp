package com.ujjwalbhardwaj.intuit.profile.enums;

import java.util.Set;

public enum ProfileEventStatus {
    CREATED,
    CREATE_ACCEPTED,
    CREATE_REJECTED,
    UPDATE_ACCEPTED,
    UPDATE_REJECTED;

    public static final Set<ProfileEventStatus> VALIDATED_STATUSES = Set.of(CREATE_ACCEPTED, UPDATE_ACCEPTED);
    public static final Set<ProfileEventStatus> INVALIDATED_STATUSES = Set.of(CREATE_REJECTED, UPDATE_REJECTED);
}
