package com.io.reactivecoroutines.weather;

import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public final class WeatherInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    @Id
    private Long id;
    private String region;
    private String country;
    private String state;
    private String city;
    private LocalDate localDate;
    private String avgTemperature;

    public WeatherInfo(
            String region,
            String country,
            String state,
            String city,
            LocalDate localDate,
            String avgTemperature
    ) {
        this.region = region;
        this.country = country;
        this.state = state;
        this.city = city;
        this.localDate = localDate;
        this.avgTemperature = avgTemperature;
    }

    public WeatherInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvgTemperature() {
        return avgTemperature;
    }

    public void setAvgTemperature(String avgTemperature) {
        this.avgTemperature = avgTemperature;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WeatherInfo) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.region, that.region) &&
                Objects.equals(this.country, that.country) &&
                Objects.equals(this.state, that.state) &&
                Objects.equals(this.city, that.city) &&
                Objects.equals(this.localDate, that.localDate) &&
                Objects.equals(this.avgTemperature, that.avgTemperature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, region, country, state, city, localDate, avgTemperature);
    }

    @Override
    public String toString() {
        return "WeatherInfo[" +
                "id=" + id + ", " +
                "region=" + region + ", " +
                "country=" + country + ", " +
                "state=" + state + ", " +
                "city=" + city + ", " +
                "localDate=" + localDate + ", " +
                "avgTemperature=" + avgTemperature + ']';
    }
}
