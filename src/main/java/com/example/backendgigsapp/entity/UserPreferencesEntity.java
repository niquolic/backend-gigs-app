package com.example.backendgigsapp.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document("user_preferences")
public class UserPreferencesEntity {

    @Id
    private String id;

    private ObjectId userId;

    private String language;

    private String currency;
}
