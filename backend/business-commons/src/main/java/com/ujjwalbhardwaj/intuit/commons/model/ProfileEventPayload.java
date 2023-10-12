package com.ujjwalbhardwaj.intuit.commons.model;

import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEventPayload {
    private String requestId;
    private String businessId;
    private ProfileDetails profileDetails;
    private Set<Product> subscribedProducts;
    private EventType eventType;

}
