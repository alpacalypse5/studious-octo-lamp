package com.ujjwalbhardwaj.intuit.commons.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String lineOne;
    private String lineTwo;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
