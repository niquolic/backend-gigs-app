package com.example.backendgigsapp.entities;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDate;

@Data
@Document(collection = "gigs")
public class GigsEntity {

    @Id
    private String id;
    private String userId;
    private String band;
    private LocalDate date;
    private String city;
    private String country;
    private String venue;
}
