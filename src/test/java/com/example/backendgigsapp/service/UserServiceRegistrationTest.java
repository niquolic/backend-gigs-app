package com.example.backendgigsapp.service;

import com.example.backendgigsapp.entity.UsersEntity;
import com.example.backendgigsapp.repository.UserRepository;
import com.example.backendgigsapp.request.UserRequest;
import com.example.backendgigsapp.security.JwtService;
import com.example.backendgigsapp.security.RevokedTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceRegistrationTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPreferencesService userPreferencesService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private RevokedTokenService revokedTokenService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerCreatesPreferencesWithOptionalRequestValues() {
        UserRequest request = new UserRequest("  test-user  ", "a-secure-password", "fr", "EUR");
        when(userRepository.findByLogin("test-user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("a-secure-password")).thenReturn("encoded-password");
        when(userRepository.save(any(UsersEntity.class))).thenAnswer(invocation -> {
            UsersEntity user = invocation.getArgument(0);
            user.setId("64ddfb9f9581fa1bfd5b0350");
            return user;
        });

        userService.register(request);

        verify(userPreferencesService).createForUser("64ddfb9f9581fa1bfd5b0350", "fr", "EUR");
    }

    @Test
    void registerDeletesUserWhenPreferencesCreationFails() {
        UserRequest request = new UserRequest("test-user", "a-secure-password", null, null);
        when(userRepository.findByLogin("test-user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("a-secure-password")).thenReturn("encoded-password");
        UsersEntity savedUser = new UsersEntity();
        savedUser.setId("64ddfb9f9581fa1bfd5b0350");
        when(userRepository.save(any(UsersEntity.class))).thenReturn(savedUser);
        doThrow(new IllegalStateException("preferences unavailable"))
                .when(userPreferencesService).createForUser(savedUser.getId(), null, null);

        assertThrows(IllegalStateException.class, () -> userService.register(request));

        verify(userRepository).delete(savedUser);
    }

    @Test
    void registerRejectsDuplicateLogin() {
        UserRequest request = new UserRequest("test-user", "a-secure-password", null, null);
        when(userRepository.findByLogin("test-user")).thenReturn(Optional.of(new UsersEntity()));

        assertThrows(IllegalArgumentException.class, () -> userService.register(request));

        verify(userRepository, never()).save(any(UsersEntity.class));
        verifyNoInteractions(userPreferencesService);
    }

    @Test
    void registerTranslatesConcurrentDuplicateLogin() {
        UserRequest request = new UserRequest("test-user", "a-secure-password", null, null);
        when(userRepository.findByLogin("test-user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("a-secure-password")).thenReturn("encoded-password");
        when(userRepository.save(any(UsersEntity.class))).thenThrow(new DuplicateKeyException("duplicate"));

        assertThrows(IllegalArgumentException.class, () -> userService.register(request));

        verifyNoInteractions(userPreferencesService);
    }
}
