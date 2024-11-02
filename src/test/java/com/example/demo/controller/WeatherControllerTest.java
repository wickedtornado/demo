package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.model.WeatherInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;


@SpringBootTest
class WeatherControllerTest {
    @Autowired
    private WeatherController weatherController;

    @Test
    void getWeatherInfo() throws JsonMappingException, JsonProcessingException {
        WeatherInfo response = weatherController.getWeatherInfo("411014", "2020-10-15");
        
    }
}