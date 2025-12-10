package com.intelcom.hora_tools.hora_tools.service;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intelcom.hora_tools.hora_tools.entity.User;
import com.intelcom.hora_tools.hora_tools.entity.UserType;
import com.intelcom.hora_tools.hora_tools.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserSyncService {

    private final UserRepository userRepository;

    @Transactional
    public User syncUserFromKeycloak(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            throw new IllegalArgumentException("Authentication is not OAuth2");
        }

        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauth2Token.getPrincipal();

        // Extract user information from Keycloak
        Map<String, Object> attributes = oauth2User.getAttributes();
        String keycloakId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");
        String preferredUsername = (String) attributes.get("preferred_username");

        log.info("Syncing user from Keycloak: {} ({})", preferredUsername, email);

        // Check if user already exists
        User user = userRepository.findById(keycloakId)
                .orElseGet(() -> createNewUser(keycloakId, email, firstName, lastName, preferredUsername));

        // Update user information
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private User createNewUser(String id, String email, String firstName, String lastName, String username) {
        log.info("Creating new user from Keycloak: {}", username);

        return User.builder()
                .id(id)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password("") // No password for OAuth users
                .enabled(true)
                .language("en")
                .isSystemAdmin(0)
                .isAutomatedTestUser(false)
                .locked(false)
                .temporaryPassword(false)
                .userType(UserType.USER)
                .build();
    }
}
