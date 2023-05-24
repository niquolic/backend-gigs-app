package com.example.backendgigsapp.controller;
import com.example.backendgigsapp.entities.UsersEntity;
import com.example.backendgigsapp.service.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    // Link the related service
    @Autowired
    ServiceUser serviceUser;

    // Get the security key
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Set the expiration time of the token
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    @GetMapping("/getUserByLoginAndPassword")
    public ResponseEntity<String> getUserByLoginAndPassword(@RequestParam() String login, @RequestParam() String password){
        // Controller linked to the flutter app

        System.out.println("User received : " + login + " " + password);

        // Call to the service method
        try {
            UsersEntity userEntity = serviceUser.getUserByLoginAndPassword(login, password);
            // Put the datas in the token
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
