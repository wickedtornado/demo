package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.WeatherInfo;
import com.example.demo.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    private final String GOOGLE_MAPS_API_KEY = "YOUR_GOOGLE_MAPS_API_KEY";
    private final String OPENWEATHER_API_KEY = "YOUR_OPENWEATHER_API_KEY";
    private final String GOOGLE_MAPS_API_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=" + GOOGLE_MAPS_API_KEY;
    private final String OPENWEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=" + OPENWEATHER_API_KEY + "&units=metric";

    @PostMapping("/{pincode}/{for_date}")
    public WeatherInfo getWeatherInfo(@PathVariable String pincode, @PathVariable String for_date) throws JsonMappingException, JsonProcessingException {
        Optional<WeatherInfo> weatherInfoOptional = weatherService.getWeatherByPincode(pincode);
        
        if (weatherInfoOptional.isPresent()) {
            return weatherInfoOptional.get();
        } else {
            // Fetch latitude and longitude from Google Maps API
            String googleMapsUrl = String.format(GOOGLE_MAPS_API_URL, pincode);
            RestTemplate restTemplate = new RestTemplate();
            String googleResponse = restTemplate.getForObject(googleMapsUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode googleJson = objectMapper.readTree(googleResponse);
            double latitude = googleJson.path("results").get(0).path("geometry").path("location").path("lat").asDouble();
            double longitude = googleJson.path("results").get(0).path("geometry").path("location").path("lng").asDouble();
            
            // Fetch weather information from OpenWeather API
            String openWeatherUrl = String.format(OPENWEATHER_API_URL, latitude, longitude);
            String weatherResponse = restTemplate.getForObject(openWeatherUrl, String.class);
            JsonNode weatherJson = objectMapper.readTree(weatherResponse);
            String weatherDescription = weatherJson.path("weather").get(0).path("description").asText();
            double temperature = weatherJson.path("main").path("temp").asDouble();
            // Create a new WeatherInfo object and save it
            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.setPincode(pincode);
            weatherInfo.setLatitude(latitude);
            weatherInfo.setLongitude(longitude);
            weatherInfo.setWeatherDescription(weatherDescription);
            weatherInfo.setTemperature(temperature);

            weatherService.saveWeatherInfo(weatherInfo);

            return weatherInfo;
        }
    }
}
