package com.example.backendgigsapp.entity;

import org.springframework.data.annotation.Id;

public class StatsCountryEntity {
    private @Id String country;
    private Long count;

    public String getCountry() {
        return country;
    }

    public Long getCount() {
        return count;
    }
}
