package com.example.backendgigsapp.service;

import com.example.backendgigsapp.entity.UsersEntity;
import com.example.backendgigsapp.repository.UserRepository;
import com.example.backendgigsapp.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceUser {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersEntity login(UserRequest userRequest) {
        UsersEntity user = userRepo.findByLogin(userRequest.getLogin())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!passwordMatches(userRequest.getPassword(), user)) {
            throw new NoSuchElementException("User not found");
        }

        return user;
    }

    /**
     * Vérifie le mot de passe. Les comptes créés avant ce fix ont leur mot
     * de passe stocké en clair : s'il correspond encore en clair, on le
     * rehash immédiatement en bcrypt et on sauvegarde, pour migrer les
     * anciens comptes sans forcer personne à réinitialiser son mot de passe.
     * Les nouveaux comptes (via register) sont hashés dès la création, donc
     * cette branche "clair" ne sert qu'une fois par compte existant.
     */
    private boolean passwordMatches(String rawPassword, UsersEntity user) {
        String stored = user.getPassword();

        if (isBcryptHash(stored)) {
            return passwordEncoder.matches(rawPassword, stored);
        }

        if (stored != null && stored.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepo.save(user);
            return true;
        }

        return false;
    }

    private boolean isBcryptHash(String value) {
        return value != null
                && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }

    public void register(UsersEntity user) {
        final Optional<UsersEntity> foundUser = userRepo.findByLogin(user.getLogin());
        if (foundUser.isPresent()) {
            throw new IllegalArgumentException("Ce login est déjà utilisé, choisis-en un autre.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
