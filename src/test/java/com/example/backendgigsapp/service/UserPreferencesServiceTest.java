package com.example.backendgigsapp.service;

import com.example.backendgigsapp.entity.UserPreferencesEntity;
import com.example.backendgigsapp.repository.UserPreferencesRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPreferencesServiceTest {

    @Mock
    private UserPreferencesRepository userPreferencesRepository;

    @InjectMocks
    private UserPreferencesService userPreferencesService;

    @Test
    void createForUserUsesEnglishAndEuroWhenValuesAreOmitted() {
        String userId = new ObjectId().toHexString();
        when(userPreferencesRepository.save(org.mockito.ArgumentMatchers.any(UserPreferencesEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userPreferencesService.createForUser(userId, null, null);

        ArgumentCaptor<UserPreferencesEntity> captor = ArgumentCaptor.forClass(UserPreferencesEntity.class);
        verify(userPreferencesRepository).save(captor.capture());

        assertEquals(new ObjectId(userId), captor.getValue().getUserId());
        assertEquals("en", captor.getValue().getLanguage());
        assertEquals("EUR", captor.getValue().getCurrency());
    }

    @Test
    void createForUserPreservesProvidedValues() {
        String userId = new ObjectId().toHexString();
        when(userPreferencesRepository.save(org.mockito.ArgumentMatchers.any(UserPreferencesEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        userPreferencesService.createForUser(userId, "fr-FR", "USD");

        ArgumentCaptor<UserPreferencesEntity> captor = ArgumentCaptor.forClass(UserPreferencesEntity.class);
        verify(userPreferencesRepository).save(captor.capture());

        assertEquals("fr-FR", captor.getValue().getLanguage());
        assertEquals("USD", captor.getValue().getCurrency());
    }
}
