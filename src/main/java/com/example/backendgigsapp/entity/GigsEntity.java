package com.example.backendgigsapp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "gigs")
public class GigsEntity {

    @Id
    private String id;

    // Index simple (pas unique) : toutes les requêtes de liste et de stats
    // filtrent sur ce champ (findByUserIdOrderByDateDesc, et toutes les
    // agrégations de StatsRepository), un index évite un scan complet de
    // la collection "gigs" à mesure qu'elle grossit.
    @Indexed
    private String userId;

    @NotEmpty(message = "Au moins un groupe est requis")
    private List<String> bands;

    @NotNull(message = "La date est requise")
    private LocalDate date;

    @NotBlank(message = "La ville est requise")
    private String city;

    @NotBlank(message = "Le pays est requis")
    private String country;

    @NotBlank(message = "Le lieu est requis")
    private String venue;

    private Double price;
}
