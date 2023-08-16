package com.example.backendgigsapp.entities;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.List;

@Data
@Document("users")
public class UsersEntity {

    @Id
    private String id;
    private String login;
    private String password;
    private List<String> gigs;

}