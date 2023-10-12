package com.ujjwalbhardwaj.intuit.server.service;

import com.ujjwalbhardwaj.intuit.commons.model.GenericResponse;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventRequest;
import com.ujjwalbhardwaj.intuit.server.client.BusinessProfileClient;
import com.ujjwalbhardwaj.intuit.server.model.GetBusinessProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BusinessProfileService {
    private final BusinessProfileClient businessProfileClient;

    public GetBusinessProfileResponse getProfile(final String businessId) throws Exception {
        return businessProfileClient.getBusinessProfile(businessId);
    }

    public GenericResponse createEvent(final ProfileEventRequest profileEventRequest, final String businessId) throws Exception {

        return businessProfileClient.createEvent(businessId, profileEventRequest);
    }

}
