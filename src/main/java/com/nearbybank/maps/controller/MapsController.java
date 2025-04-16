package com.nearbybank.maps.controller;

import com.nearbybank.maps.model.Bank;
import com.nearbybank.maps.service.MapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maps")
public class MapsController {

    @Autowired
    private MapsService mapsService;
    
    @GetMapping("/")
    public String testing() {
    	return "maps-service working";
    }

    @GetMapping("/coordinates")
    public String[] getCoordinates(@RequestParam("zipcode") String zipcode) {
        return mapsService.getCoordinates(zipcode);
    }

    @GetMapping("/banks")
    public List<Bank> getNearbyBanks(@RequestParam("lat") String lat, @RequestParam("lng") String lng) {
        return mapsService.getNearbyBanks(lat, lng);
    }
}
