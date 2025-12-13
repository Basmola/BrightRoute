package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.service.UserService;
import com.brightroute.brightroute.model.UserStudentRegistrationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Simple DTO for login request to avoid incomplete User entity
class LoginRequest {
    public String email;
    public String password;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handles unified registration for both the base User and the Student profile.
     * Takes a DTO containing all necessary fields.
     * Maps to: POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserStudentRegistrationDto registrationDto) {
        // Delegates the transactional save of both User and Student to the service.
        User registeredUser = userService.registerUserAndStudent(registrationDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED); // Returns 201 Created on success
    }

    /**
     * Handles user login authentication.
     * Maps to: POST /api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestBody LoginRequest request // Use a DTO for cleaner input
    ) {
        // Service handles password verification and user lookup
        return ResponseEntity.ok(userService.login(request.getEmail(), request.getPassword()));
    }

    /**
     * Handles updating the user's editable profile fields (first name, last name, phone number).
     * Maps to: PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(
            @PathVariable Integer id,
            @RequestBody User updatedUser
    ) {
        return ResponseEntity.ok(userService.updateProfile(id, updatedUser));
    }

    /**
     * Retrieves a single user's profile data by ID.
     * Maps to: GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> viewUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.viewUser(id));
    }

    /**
     * Verifies a user's identity based on ID, names, and password.
     * Maps to: POST /api/users/verify
     */
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(
            @RequestParam Integer userId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String password
    ) {
        return ResponseEntity.ok(
                userService.verifyIdentity(userId, firstName, lastName, password)
        );
    }
    
    /**
     * Handles user logout (primarily for server-side session cleanup if applicable).
     * Maps to: POST /api/users/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Integer userId) {
        userService.logout(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Returns 204 No Content
    }
}