package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.UsersEntity;
import com.example.backendgigsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceUser {

    // Link the related repository
    @Autowired
    UserRepository userRepo;

    public UsersEntity getUserByLoginAndPassword(String login, String password){

        // Call to the repository method
        UsersEntity us = userRepo.findByLoginAndPassword(login, password).orElseThrow();

        return us;

    }

}
