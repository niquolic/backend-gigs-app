package com.example.backendgigsapp.entities;
import jakarta.persistence.*;
import lombok.Data;

import java.text.DateFormat;

@Data
@Entity
@Table(name = "gigs")
public class GigsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    private Long id_user;
    private String band;
    private DateFormat date;
    private String city;
    private String country;
    private String venue;

}
