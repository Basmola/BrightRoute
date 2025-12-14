package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.service.UserService;
import com.brightroute.brightroute.model.UserStudentRegistrationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

class LoginRequest {
    public String email;
    public String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserStudentRegistrationDto registrationDto) {
         
        User registeredUser = userService.registerUserAndStudent(registrationDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);  
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(
            @RequestBody LoginRequest request  
    ) {
         
        return ResponseEntity.ok(userService.login(request.getEmail(), request.getPassword()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(
            @PathVariable Integer id,
            @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateProfile(id, updatedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> viewUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.viewUser(id));
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verify(
            @RequestParam Integer userId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String password) {
        return ResponseEntity.ok(
                userService.verifyIdentity(userId, firstName, lastName, password));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam Integer userId) {
        userService.logout(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserStudentRegistrationDto> getProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<User> updateFullProfile(
            @PathVariable Integer id,
            @RequestBody UserStudentRegistrationDto dto) {
        return ResponseEntity.ok(userService.updateFullProfile(id, dto));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}