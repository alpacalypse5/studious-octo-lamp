package com.ujjwalbhardwaj.intuit.profile.api.controller;

import com.ujjwalbhardwaj.intuit.profile.enums.ValidationStatus;
import com.ujjwalbhardwaj.intuit.profile.exception.BusinessProfileNotFoundException;
import com.ujjwalbhardwaj.intuit.profile.exception.InvalidRequestException;
import com.ujjwalbhardwaj.intuit.profile.model.ErrorResponse;
import com.ujjwalbhardwaj.intuit.profile.model.GetBusinessProfileResponse;
import com.ujjwalbhardwaj.intuit.profile.model.ProfileEventRequest;
import com.ujjwalbhardwaj.intuit.profile.model.ValidationResult;
import com.ujjwalbhardwaj.intuit.profile.service.ProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBusinessProfile() {
        String businessId = "someBusinessId";
        Mockito.doReturn(GetBusinessProfileResponse.builder().id("id").build())
                .when(profileService).getBusinessProfile(businessId);

        ResponseEntity<?> response = profileController.getBusinessProfile(businessId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Mockito.verify(profileService).getBusinessProfile(businessId);
    }

    @Test
    public void testGetBusinessProfile_NotFound() {
        String businessId = "someBusinessId";
        when(profileService.getBusinessProfile(businessId)).thenThrow(BusinessProfileNotFoundException.class);

        ResponseEntity<?> response = profileController.getBusinessProfile(businessId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        Mockito.verify(profileService).getBusinessProfile(businessId);
    }

    @Test
    public void testGetBusinessProfile_InternalServerError() {
        String businessId = "someBusinessId";
        when(profileService.getBusinessProfile(businessId)).thenThrow(RuntimeException.class);

        ResponseEntity<?> response = profileController.getBusinessProfile(businessId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);

        Mockito.verify(profileService).getBusinessProfile(businessId);
    }

    @Test
    public void testCreateEvent() throws Exception {
        ProfileEventRequest eventRequest = new ProfileEventRequest();
        String businessId = "someBusinessId";
        Mockito.doNothing().when(profileService).createEvent(eventRequest, businessId);

        ResponseEntity<?> response = profileController.createEvent(eventRequest, businessId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Mockito.verify(profileService).createEvent(eventRequest, businessId);
    }

    @Test
    public void testCreateEvent_BadRequest() throws Exception {
        ProfileEventRequest eventRequest = new ProfileEventRequest();
        String businessId = "someBusinessId";
        Mockito.doThrow(InvalidRequestException.class).when(profileService).createEvent(eventRequest, businessId);

        ResponseEntity<?> response = profileController.createEvent(eventRequest, businessId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);

        Mockito.verify(profileService).createEvent(eventRequest, businessId);
    }

    @Test
    void testGetUpdateStatus() {
        String businessId = "someBusinessId";
        Mockito.doReturn(ValidationResult.builder().status(ValidationStatus.ACCEPTED).build()).when(profileService).getUpdateStatus(businessId);
        ResponseEntity<?> response = profileController.getUpdateStatus(businessId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Mockito.verify(profileService).getUpdateStatus(businessId);
    }

}
