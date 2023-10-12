package com.ujjwalbhardwaj.intuit.profile.model;

import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEventRequest {
    private EventType eventType;
    private ProfileDetails payload;
    private Product source;
}
