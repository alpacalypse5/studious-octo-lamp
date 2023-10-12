package com.ujjwalbhardwaj.intuit.profile.entity.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.ujjwalbhardwaj.intuit.commons.enums.Product;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductListConvertor implements DynamoDBTypeConverter<String, Set<Product>> {
    @Override
    public String convert(Set<Product> products) {
        return products.stream()
                .map(Product::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<Product> unconvert(String s) {
        return Arrays.stream(s.split(","))
                .map(Product::valueOf)
                .collect(Collectors.toSet());
    }
}
