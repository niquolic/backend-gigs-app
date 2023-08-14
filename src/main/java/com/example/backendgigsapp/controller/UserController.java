package com.example.backendgigsapp.controller;
import com.example.backendgigsapp.entities.UsersEntity;
import com.example.backendgigsapp.service.ServiceUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private ServiceUser serviceUser;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    @GetMapping("/getUserByLoginAndPassword")
    public ResponseEntity<String> getUserByLoginAndPassword(@RequestParam String login, @RequestParam String password) {
        try {
            UsersEntity userEntity = serviceUser.getUserByLoginAndPassword(login, password);
            String token = Jwts.builder()
                    .setSubject(userEntity.getLogin())
                    .claim("userId", userEntity.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            return ResponseEntity.ok(token);
        } catch (NoSuchElementException e) {
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }
}
