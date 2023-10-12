package com.ujjwalbhardwaj.intuit.profile.entity.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class ProfileDetails {
    @DynamoDBAttribute
    private String companyName;

    @DynamoDBAttribute
    private String legalName;

    @DynamoDBAttribute
    private Address businessAddress;

    @DynamoDBAttribute
    private Address legalAddress;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String website;
}
