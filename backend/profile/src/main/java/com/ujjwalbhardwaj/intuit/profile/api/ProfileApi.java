package com.ujjwalbhardwaj.intuit.profile.api;

import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import com.ujjwalbhardwaj.intuit.profile.model.ProfileEventRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is the main API for managing business profiles.
 * It provides endpoints for fetching the business profile, creating events associated with a profile,
 * and checking the status of profile updates.
 */
@RequestMapping("/v1/profile")
public interface ProfileApi {

    /**
     * Fetch the business profile for a given business ID.
     *
     * @param businessId The ID of the business for which the profile is to be fetched.
     * @return A response entity containing the business profile.
     */
    @GetMapping
    ResponseEntity<?> getBusinessProfile(@RequestHeader("X-Business-Id") String businessId);

    /**
     * Create an event associated with a business profile.
     *
     * @param eventRequest The details of the event to be created.
     * @param businessId (Optional) The ID of the business for which the event is to be created.
     * @return A response entity indicating the result of the event creation.
     * @throws Exception If there's an error during the event creation.
     */
    @PostMapping("/event")
    ResponseEntity<?> createEvent(@RequestBody ProfileEventRequest eventRequest,
            @RequestHeader(value = "X-Business-Id") final String businessId) throws Exception;

    /**
     * Check the update status of a business profile.
     *
     * @param businessId (Optional) The ID of the business for which the update status is to be checked.
     * @return A response entity containing the update status of the business profile.
     */
    @GetMapping("/update/status")
    ResponseEntity<ValidationResult> getUpdateStatus(
            @RequestHeader(value = "X-Business-Id") String businessId);
}
