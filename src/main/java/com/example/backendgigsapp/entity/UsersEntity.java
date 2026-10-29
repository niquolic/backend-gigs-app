package com.example.backendgigsapp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document("users")
public class UsersEntity {

    @Id
    private String id;

    // The unique index prevents duplicate logins even during concurrent registrations.
    @Indexed(unique = true)
    private String login;

    private String password;

    // Gigs are stored in the separate "gigs" collection and linked through userId.
}
