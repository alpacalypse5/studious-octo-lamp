package com.ujjwalbhardwaj.intuit.server.controller;

import com.ujjwalbhardwaj.intuit.commons.model.GenericResponse;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventRequest;
import com.ujjwalbhardwaj.intuit.server.service.BusinessProfileService;
import com.ujjwalbhardwaj.intuit.server.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/business-profile")
public class BusinessProfileController {
    private final BusinessProfileService businessService;
    private final RateLimiterService rateLimiterService;

    @PostMapping("/event")
    public ResponseEntity<?> createProfile(@RequestBody final ProfileEventRequest request,
                                           @RequestHeader("X-Business-Id") final String businessId) throws Exception {
        Bucket bucket = rateLimiterService.resolveBucket(businessId);
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(businessService.createEvent(request, businessId));
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                    GenericResponse.builder().message("Too many requests").build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("X-Business-Id") final String businessId) throws Exception {
        return ResponseEntity.ok(businessService.getProfile(businessId));
    }
}
