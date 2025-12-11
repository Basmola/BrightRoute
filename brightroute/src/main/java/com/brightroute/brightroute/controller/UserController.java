package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // -------- REGISTER --------
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    // -------- LOGIN --------
    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestBody User user
    ) {
        return ResponseEntity.ok(userService.login(user.getEmail(), user.getPassword()));
    }

    // -------- UPDATE PROFILE --------
    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long id,
            @RequestBody User updatedUser
    ) {
        return ResponseEntity.ok(userService.updateProfile(id, updatedUser));
    }

    // -------- VIEW USER --------
    @GetMapping("/{id}")
    public ResponseEntity<User> viewUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.viewUser(id));
    }

    // -------- VERIFY IDENTITY --------
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(
            @RequestParam Long userId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String password
    ) {
        return ResponseEntity.ok(
                userService.verifyIdentity(userId, firstName, lastName, password)
        );
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Long userId) {
        userService.logout(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}