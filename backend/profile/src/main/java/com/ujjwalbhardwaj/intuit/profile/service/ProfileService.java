package com.ujjwalbhardwaj.intuit.profile.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ujjwalbhardwaj.intuit.profile.model.GetBusinessProfileResponse;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import com.ujjwalbhardwaj.intuit.profile.model.ProfileEventRequest;

/**
 * Service interface for managing business profiles.
 * Provides methods for fetching a business profile, creating events associated with a profile,
 * and checking the status of profile updates.
 */
public interface ProfileService {

    /**
     * Retrieves the business profile for the given business ID.
     *
     * @param businessId The ID of the business whose profile is to be fetched.
     * @return A response containing the details of the business profile.
     */
    GetBusinessProfileResponse getBusinessProfile(String businessId);

    /**
     * Creates an event associated with a given business profile.
     *
     * @param eventRequest The details of the event to be created.
     * @param businessId The ID of the business for which the event is to be associated.
     * @throws JsonProcessingException If there's an error during processing the event request.
     */
    void createEvent(ProfileEventRequest eventRequest, String businessId) throws JsonProcessingException;

    /**
     * Retrieves the update status of the business profile for the given business ID.
     *
     * @param businessId The ID of the business whose profile update status is to be fetched.
     * @return A response containing the update status of the business profile.
     */
    ValidationResult getUpdateStatus(String businessId);
}
