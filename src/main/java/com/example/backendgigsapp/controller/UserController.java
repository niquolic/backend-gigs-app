package com.example.backendgigsapp.controller;

import com.example.backendgigsapp.entity.UsersEntity;
import com.example.backendgigsapp.request.UserRequest;
import com.example.backendgigsapp.security.JwtService;
import com.example.backendgigsapp.service.ServiceUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private ServiceUser serviceUser;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserRequest userRequest) {
        UsersEntity userEntity = serviceUser.login(userRequest);
        String token = jwtService.generateToken(userEntity.getLogin(), userEntity.getId());
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
