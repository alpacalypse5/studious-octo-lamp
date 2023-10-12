package com.ujjwalbhardwaj.intuit.server.model;

import com.ujjwalbhardwaj.intuit.commons.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBusinessProfileResponse {
    private String id;
    private String companyName;
    private String legalName;
    private Address businessAddress;
    private Address legalAddress;
    private String email;
    private String website;

}
