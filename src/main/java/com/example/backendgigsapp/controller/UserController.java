package com.example.backendgigsapp.controller;
import com.example.backendgigsapp.entity.UsersEntity;
import com.example.backendgigsapp.request.UserRequest;
import com.example.backendgigsapp.service.ServiceUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {
    "https://angular-frontend-513726246973.europe-west1.run.app",
    "http://localhost:4200"
})
public class UserController {

    @Autowired
    private ServiceUser serviceUser;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("2592000000") //30 days token
    private Long jwtExpirationMs;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserRequest userRequest) {
        UsersEntity userEntity = serviceUser.login(userRequest);
        String token = Jwts.builder()
            .setSubject(userEntity.getLogin())
            .claim("userId", userEntity.getId())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRequest userRequest) {
        UsersEntity newUser = new UsersEntity();
        newUser.setLogin(userRequest.getLogin());
        newUser.setPassword(userRequest.getPassword());
        serviceUser.register(newUser);
        return ResponseEntity.ok("User registered successfully. Please log in to your account.");
    }
}
