package com.example.backendgigsapp.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "gigs")
public class GigsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 11)
    private Long id;

    @Column(name = "id_user")
    private Long userId;

    private String band;
    private LocalDate date;
    private String city;
    private String country;
    private String venue;

}
