package com.example.weathernafees.Models;

import java.util.List;

public class WeatherData {

    private List<weather> weather;
    private main main;
    private String name;

    public WeatherData(List<com.example.weathernafees.Models.weather> weather, com.example.weathernafees.Models.main main, String name) {
        this.weather = weather;
        this.main = main;
        this.name = name;
    }

    public List<com.example.weathernafees.Models.weather> getWeather() {
        return weather;
    }

    public void setWeather(List<com.example.weathernafees.Models.weather> weather) {
        this.weather = weather;
    }

    public com.example.weathernafees.Models.main getMain() {
        return main;
    }

    public void setMain(com.example.weathernafees.Models.main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
