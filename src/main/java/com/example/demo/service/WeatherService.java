package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.WeatherInfo;
import com.example.demo.repo.WeatherInfoRepository;



@Service
public class WeatherService {
    @Autowired
    private WeatherInfoRepository weatherRepository;

    public Optional<WeatherInfo> getWeatherByPincode(String pincode) {
        return weatherRepository.findByPincode(pincode);
    }

    public WeatherInfo saveWeatherInfo(WeatherInfo weatherInfo) {
        return weatherRepository.save(weatherInfo);
    }
}