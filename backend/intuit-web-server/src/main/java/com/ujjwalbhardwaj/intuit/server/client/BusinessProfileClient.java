package com.ujjwalbhardwaj.intuit.server.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ujjwalbhardwaj.intuit.commons.model.GenericResponse;
import com.ujjwalbhardwaj.intuit.commons.model.ProfileEventRequest;
import com.ujjwalbhardwaj.intuit.server.model.GetBusinessProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Log4j2
@Component
@RequiredArgsConstructor
public class BusinessProfileClient {
    private static final String BASE_URL = "http://localhost:8091";
    private final ObjectMapper objectMapper;

    public GetBusinessProfileResponse getBusinessProfile(final String businessId) throws Exception {
        final String url = String.format("%s/v1/profile", BASE_URL);
        HttpResponse response = Request.Get(url)
                .addHeader(new BasicHeader("X-Business-Id", businessId))
                .execute()
                .returnResponse();

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String json = StreamUtils.copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8);
            return objectMapper.readValue(json, new TypeReference<>() {});
        }

        log.error("Failed calling API with statusCode: " + statusCode);
        return GetBusinessProfileResponse.builder().build();
    }

    public GenericResponse createEvent(final String businessId, final ProfileEventRequest profileEventRequest) throws Exception {
        final String url = String.format("%s/v1/profile/event", BASE_URL);
        String json = Request.Post(url)
                .addHeader(new BasicHeader("X-Business-Id", businessId))
                .addHeader(new BasicHeader("Content-Type", "application/json"))
                .bodyString(objectMapper.writeValueAsString(profileEventRequest), ContentType.APPLICATION_JSON)
                .execute()
                .handleResponse(defaultHandler());
        return objectMapper.readValue(json, new TypeReference<>() {});
    }

    private ResponseHandler<String> defaultHandler() {
        return httpResponse -> StreamUtils.copyToString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
    }
}
