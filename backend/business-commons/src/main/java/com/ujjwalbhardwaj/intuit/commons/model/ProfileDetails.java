package com.ujjwalbhardwaj.intuit.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDetails {
    private String companyName;
    private String legalName;
    private Address businessAddress;
    private Address legalAddress;
    private String email;
    private String website;
}

