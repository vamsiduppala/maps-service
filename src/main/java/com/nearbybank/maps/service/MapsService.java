package com.nearbybank.maps.service;

import com.nearbybank.maps.integration.GoogleMapsClient;
import com.nearbybank.maps.model.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapsService {

    private final GoogleMapsClient googleMapsClient;

    @Autowired
    public MapsService(GoogleMapsClient googleMapsClient) {
        this.googleMapsClient = googleMapsClient;
    }

    public String[] getCoordinates(String zipcode) {
        return googleMapsClient.getCoordinates(zipcode);
    }

    public List<Bank> getNearbyBanks(String lat, String lng) {
        return googleMapsClient.getNearbyBanks(lat, lng);
    }
}
