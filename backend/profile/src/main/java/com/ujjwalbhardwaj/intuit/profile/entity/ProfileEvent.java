package com.ujjwalbhardwaj.intuit.profile.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.ujjwalbhardwaj.intuit.commons.enums.EventType;
import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import com.ujjwalbhardwaj.intuit.profile.entity.model.ProfileDetails;
import com.ujjwalbhardwaj.intuit.profile.enums.ProfileEventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "ProfileEvent")
public class ProfileEvent {
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String businessId;

    @DynamoDBTypeConvertedEnum
    private EventType eventType;

    @DynamoDBAttribute
    private ProfileDetails payload;

    @DynamoDBTypeConvertedEnum
    private Product source;

    @DynamoDBTypeConvertedEnum
    private ProfileEventStatus status;

    @DynamoDBAttribute
    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
    private Date createdAt;
}

