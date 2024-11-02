package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.LocationInfo;
import com.example.demo.repo.LocationInfoRepository;



@Service
public class GeocodingService {
	
	@Value("${geocoding.api.key}")
	private String geocodingApiKey;
    
    private final LocationInfoRepository locationInfoRepository;
    private final RestTemplate restTemplate;
    
    public GeocodingService(LocationInfoRepository locationInfoRepository) {
        this.locationInfoRepository = locationInfoRepository;
        this.restTemplate = new RestTemplate();
    }
    
    public LocationInfo getLocationInfo(String pincode) {
        return locationInfoRepository.findByPincode(pincode)
                .orElseGet(() -> fetchAndSaveLocationInfo(pincode));
    }
    
    private LocationInfo fetchAndSaveLocationInfo(String pincode) {
       
        LocationInfo locationInfo = new LocationInfo();
        locationInfo.setPincode(pincode);
        locationInfo.setLatitude(18.5204);
        locationInfo.setLongitude(73.8567);
        
        return locationInfoRepository.save(locationInfo);
    }
}
