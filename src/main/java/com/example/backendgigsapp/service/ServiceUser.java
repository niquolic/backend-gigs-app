package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entity.UsersEntity;
import com.example.backendgigsapp.repository.UserRepository;
import com.example.backendgigsapp.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceUser {

    @Autowired
    private UserRepository userRepo;

    public UsersEntity login(UserRequest userRequest) {
        return userRepo.findByLoginAndPassword(userRequest.getLogin(), userRequest.getPassword())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public void register(UsersEntity user) {
        final Optional<UsersEntity> foundUser = userRepo.findByLogin(user.getLogin());
        if (foundUser.isPresent()) {
            throw new IllegalArgumentException("User with this identifier already exists. PLease choose another identifier.");
        }
        userRepo.save(user);
    }

    public boolean addGigsToUser(String gigId, String userId) {
        Optional<UsersEntity> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            UsersEntity user = userOptional.get();
            List<String> gigsList = user.getGigs();
            gigsList.add(gigId);
            user.setGigs(gigsList);
            userRepo.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteGig(String userId, String gigId){
        Optional<UsersEntity> userOptional = userRepo.findById(userId);
        if(userOptional.isPresent()){
            UsersEntity user = userOptional.get();
            List<String> gigsList = user.getGigs();
            gigsList.remove(gigId);
            user.setGigs(gigsList);
            userRepo.save(user);
            return true;
        }else {
            return false;
        }
    }

}
