package com.example.backendgigsapp.entity;
import org.springframework.data.annotation.Id;

public class StatsBandsEntity {
    private @Id String band;
    private Long count;

    public String getBand() {
        return band;
    }

    public Long getCount() {
        return count;
    }
}