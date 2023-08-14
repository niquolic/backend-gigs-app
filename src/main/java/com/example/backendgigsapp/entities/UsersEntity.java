package com.example.backendgigsapp.entities;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Data
@Document("users")
public class UsersEntity {

    @Id
    private String id;
    private String login;
    private String password;

}