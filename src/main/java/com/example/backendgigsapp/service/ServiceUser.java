package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.UsersEntity;
import com.example.backendgigsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ServiceUser {

    @Autowired
    private UserRepository userRepo;

    public UsersEntity getUserByLoginAndPassword(String login, String password) {
        return userRepo.findByLoginAndPassword(login, password)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }
}
