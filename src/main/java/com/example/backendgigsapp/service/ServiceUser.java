package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.UsersEntity;
import com.example.backendgigsapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceUser {

    @Autowired
    private UserRepository userRepo;

    public UsersEntity getUserByLoginAndPassword(String login, String password) {
        return userRepo.findByLoginAndPassword(login, password)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public String addGigsToUser(String gigId, String userId) {
        Optional<UsersEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            UsersEntity user = userOptional.get();
            List<String> gigsList = user.getGigs();
            gigsList.add(gigId);
            user.setGigs(gigsList);
            userRepo.save(user);

            return "ok";
        } else {
            return "User not found";
        }
    }

}
