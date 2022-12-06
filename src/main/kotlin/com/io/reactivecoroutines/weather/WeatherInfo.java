package com.io.reactivecoroutines.weather;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
public final class WeatherInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;
        @jakarta.persistence.Id
        @Id
        @GeneratedValue
        private Long id;
        private String region;
        private String country;
        private String state;
        private String city;
        private Integer month;
        private Integer day;
        private Integer year;
        private String avgTemperature;

        public WeatherInfo(
                Long id,
                String region,
                String country,
                String state,
                String city,
                Integer month,
                Integer day,
                Integer year,
                String avgTemperature
        ) {
                this.id = id;
                this.region = region;
                this.country = country;
                this.state = state;
                this.city = city;
                this.month = month;
                this.day = day;
                this.year = year;
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

        public Integer getMonth() {
                return month;
        }

        public void setMonth(Integer month) {
                this.month = month;
        }

        public Integer getDay() {
                return day;
        }

        public void setDay(Integer day) {
                this.day = day;
        }

        public Integer getYear() {
                return year;
        }

        public void setYear(Integer year) {
                this.year = year;
        }

        public String getAvgTemperature() {
                return avgTemperature;
        }

        public void setAvgTemperature(String avgTemperature) {
                this.avgTemperature = avgTemperature;
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
                        Objects.equals(this.month, that.month) &&
                        Objects.equals(this.day, that.day) &&
                        Objects.equals(this.year, that.year) &&
                        Objects.equals(this.avgTemperature, that.avgTemperature);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, region, country, state, city, month, day, year, avgTemperature);
        }

        @Override
        public String toString() {
                return "WeatherInfo[" +
                        "id=" + id + ", " +
                        "region=" + region + ", " +
                        "country=" + country + ", " +
                        "state=" + state + ", " +
                        "city=" + city + ", " +
                        "month=" + month + ", " +
                        "day=" + day + ", " +
                        "year=" + year + ", " +
                        "avgTemperature=" + avgTemperature + ']';
        }
}
