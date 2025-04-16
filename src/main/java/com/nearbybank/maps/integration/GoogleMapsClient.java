package com.nearbybank.maps.integration;

import com.nearbybank.maps.model.Bank;
import com.nearbybank.maps.model.GeoCodeResponse;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoogleMapsClient {

    static Dotenv dotenv = Dotenv.load();

    private final static String GOOGLE_MAPS_API_KEY = dotenv.get("GOOGLE_MAPS_API_KEY");
    private static final String GEOCODE_API_URL =
            "https://maps.googleapis.com/maps/api/geocode/json?address={zipcode}&key=" + GOOGLE_MAPS_API_KEY;
    private static final String PLACES_API_URL =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location={lat},{lng}&radius=16093&type=bank&key=" + GOOGLE_MAPS_API_KEY;

    private final RestTemplate restTemplate;

    @Autowired
    public GoogleMapsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String[] getCoordinates(String zipcode) {
        GeoCodeResponse response = restTemplate.getForObject(GEOCODE_API_URL, GeoCodeResponse.class, zipcode);

        if (response != null && response.results != null && !response.results.isEmpty()) {
            double lat = response.results.get(0).geometry.location.lat;
            double lng = response.results.get(0).geometry.location.lng;
            return new String[]{String.valueOf(lat), String.valueOf(lng)};
        }

        return new String[0]; // Or throw exception
    }

    public List<Bank> getNearbyBanks(String lat, String lng) {
        String finalUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
            + "?location=" + lat + "," + lng
            + "&radius=16093&type=bank&key=" + GOOGLE_MAPS_API_KEY;

        System.out.println("Calling URL: " + finalUrl);

        try {
            ResponseEntity<GeoCodeResponse> response = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<GeoCodeResponse>() {}
            );

            GeoCodeResponse body = response.getBody();

            if (body == null || body.results == null || body.results.isEmpty()) {
                System.out.println("No nearby banks found for location: " + lat + "," + lng);
                return Collections.emptyList();
            }

            // Convert Result -> Bank
            return body.results.stream().map(result -> {
                String name = result.name != null ? result.name : "Unknown Bank";
                String address = result.vicinity != null ? result.vicinity : "Unknown Address";
                String latitudeStr = String.valueOf(result.geometry.location.lat);
                String longitudeStr = String.valueOf(result.geometry.location.lng);

                return new Bank(name, address, latitudeStr, longitudeStr);
            }).collect(Collectors.toList());

        } catch (RestClientException e) {
            System.err.println("Error while calling Google Maps API: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
