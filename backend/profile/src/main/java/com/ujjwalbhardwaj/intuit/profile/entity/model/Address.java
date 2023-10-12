package com.ujjwalbhardwaj.intuit.profile.entity.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@DynamoDBDocument
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @DynamoDBAttribute
    private String lineOne;

    @DynamoDBAttribute
    private String lineTwo;

    @DynamoDBAttribute
    private String city;

    @DynamoDBAttribute
    private String state;

    @DynamoDBAttribute
    private String zipCode;

    @DynamoDBAttribute
    private String country;
}
