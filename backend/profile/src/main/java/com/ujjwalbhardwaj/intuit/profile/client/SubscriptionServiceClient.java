package com.ujjwalbhardwaj.intuit.profile.client;

import com.ujjwalbhardwaj.intuit.commons.enums.Product;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SubscriptionServiceClient {

    public Set<Product> getSubscribedProducts(final String businessId) {
        return Set.of(Product.PAYROLL, Product.ACCOUNTING);
    }
}
