package com.example.backendgigsapp.service;

import com.example.backendgigsapp.entity.UserPreferencesEntity;
import com.example.backendgigsapp.repository.UserPreferencesRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class UserPreferencesService {

    private static final String DEFAULT_LANGUAGE = "en";
    private static final String DEFAULT_CURRENCY = "EUR";

    private final UserPreferencesRepository userPreferencesRepository;

    public UserPreferencesService(UserPreferencesRepository userPreferencesRepository) {
        this.userPreferencesRepository = userPreferencesRepository;
    }

    public UserPreferencesEntity createForUser(String userId, String language, String currency) {
        UserPreferencesEntity preferences = new UserPreferencesEntity();
        preferences.setUserId(new ObjectId(userId));
        preferences.setLanguage(language != null ? language : DEFAULT_LANGUAGE);
        preferences.setCurrency(currency != null ? currency : DEFAULT_CURRENCY);

        return userPreferencesRepository.save(preferences);
    }
}
