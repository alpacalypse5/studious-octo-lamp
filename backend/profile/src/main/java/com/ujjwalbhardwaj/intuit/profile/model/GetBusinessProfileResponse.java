package com.ujjwalbhardwaj.intuit.profile.model;

import com.ujjwalbhardwaj.intuit.profile.entity.model.Address;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetBusinessProfileResponse {
    private final String id;
    private final String companyName;
    private final String legalName;
    private final Address businessAddress;
    private final Address legalAddress;
    private final String email;
    private final String website;

}
