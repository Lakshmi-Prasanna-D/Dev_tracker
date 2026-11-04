package com.devtrack.devtrack.service;

import com.devtrack.devtrack.dto.UserRegistrationDto;
import com.devtrack.devtrack.entity.Activity;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ActivityType;
import com.devtrack.devtrack.enums.Role;
import com.devtrack.devtrack.exception.ResourceNotFoundException;
import com.devtrack.devtrack.repository.ActivityRepository;
import com.devtrack.devtrack.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       ActivityRepository activityRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername().trim())) {
            throw new IllegalArgumentException("Username '" + dto.getUsername().trim() + "' is already taken.");
        }
        if (userRepository.existsByEmail(dto.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email '" + dto.getEmail().trim() + "' is already registered.");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setFullName(dto.getFullName().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Record welcome activity
        Activity welcomeActivity = new Activity(
                savedUser,
                ActivityType.GOAL_CREATED,
                "Welcome to DevTrack! Account registered successfully.",
                "USER",
                savedUser.getId(),
                LocalDateTime.now()
        );
        activityRepository.save(welcomeActivity);

        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();
            return userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                            .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found: " + username)));
        }

        // Fallback for demo / unauthenticated contexts if needed
        return userRepository.findByUsername("alex")
                .or(() -> userRepository.findAll().stream().findFirst())
                .orElse(null);
    }

    @Transactional
    public User createOrUpdateUser(String username, String email, String rawPassword, String fullName, Role role) {
        return userRepository.findByUsername(username).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(rawPassword));
            newUser.setFullName(fullName);
            newUser.setRole(role);
            newUser.setEnabled(true);
            newUser.setCreatedAt(LocalDateTime.now());
            return userRepository.save(newUser);
        });
    }
}
