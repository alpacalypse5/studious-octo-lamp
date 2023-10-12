package com.ujjwalbhardwaj.intuit.profile.entity.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringListConvertor implements DynamoDBTypeConverter<String, List<String>> {
    @Override
    public String convert(List<String> reasons) {
        return String.join(",", reasons);
    }

    @Override
    public List<String> unconvert(String s) {
        return Arrays.stream(s.split(","))
                .collect(Collectors.toList());
    }
}
