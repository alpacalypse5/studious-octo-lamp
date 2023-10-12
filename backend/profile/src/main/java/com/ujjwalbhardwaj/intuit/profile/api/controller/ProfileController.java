package com.ujjwalbhardwaj.intuit.profile.api.controller;

import com.ujjwalbhardwaj.intuit.commons.model.GenericResponse;
import com.ujjwalbhardwaj.intuit.profile.api.ProfileApi;
import com.ujjwalbhardwaj.intuit.profile.exception.BusinessProfileNotFoundException;
import com.ujjwalbhardwaj.intuit.profile.exception.InvalidRequestException;
import com.ujjwalbhardwaj.intuit.profile.model.ErrorResponse;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import com.ujjwalbhardwaj.intuit.profile.model.ProfileEventRequest;
import com.ujjwalbhardwaj.intuit.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController implements ProfileApi {
    private final ProfileService profileService;

    @Override
    public ResponseEntity<?> getBusinessProfile(final String businessId) {
        try {
            return ResponseEntity.ok(profileService.getBusinessProfile(businessId));
        } catch (final BusinessProfileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (final Exception e) {
            return ResponseEntity.internalServerError().body(ErrorResponse.builder().message(e.getMessage()).build());
        }
    }

    @Override
    public ResponseEntity<?> createEvent(final ProfileEventRequest eventRequest, final String businessId) throws Exception {
        try {
            profileService.createEvent(eventRequest, businessId);
            return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.builder()
                            .message("Event registered successfully")
                    .build());
        } catch (final InvalidRequestException e) {
            return ResponseEntity.badRequest().body(ErrorResponse.builder().message(e.getMessage()).build());
        } catch (final Exception e) {
            return ResponseEntity.internalServerError().body(ErrorResponse.builder().message(e.getMessage()).build());
        }
    }

    @Override
    public ResponseEntity<ValidationResult> getUpdateStatus(String businessId) {
        return ResponseEntity.ok(profileService.getUpdateStatus(businessId));
    }


}