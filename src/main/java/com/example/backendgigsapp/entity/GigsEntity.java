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

    // Indexed because list and statistics queries filter by this field.
    @Indexed
    private String userId;

    @NotEmpty(message = "At least one band is required")
    private List<String> bands;

    @NotNull(message = "A date is required")
    private LocalDate date;

    @NotBlank(message = "A city is required")
    private String city;

    @NotBlank(message = "A country is required")
    private String country;

    @NotBlank(message = "A venue is required")
    private String venue;

    private Double price;
}
