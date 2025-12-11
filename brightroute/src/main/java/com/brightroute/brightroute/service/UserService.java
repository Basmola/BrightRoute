package com.brightroute.brightroute.service;
import com.brightroute.brightroute.repository.UserRepository;

import com.brightroute.brightroute.enums.AccountStatus;
import com.brightroute.brightroute.enums.Role;
import com.brightroute.brightroute.model.User;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; 

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(User user){
        // 1. Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        // 2. Set defaults using Enums
        if(user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        if(user.getAccountStatus() == null){
            user.setAccountStatus(AccountStatus.ACTIVE); // Uses Enum
        }
        if(user.getRole() == null){
            user.setRole(Role.STUDENT); // Uses Enum
        }
        return userRepository.save(user);
    }

    public User login(String email, String rawPassword){
        User user = userRepository.findByEmail(email)
                      .orElseThrow(()->new RuntimeException("Invalid credentials or User not found"));
        
        // Check hashed password
        if(passwordEncoder.matches(rawPassword, user.getPasswordHash())){
            return user;      
        }
        else{
            throw new RuntimeException("Invalid credentials or User not found");
        }
    }

    @Transactional
    public User updateProfile(Integer id, User updatedUser){
        User user = viewUser(id);
        
        if(updatedUser.getFirstName() != null){
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null){
            user.setLastName(updatedUser.getLastName());
        } 
        if (updatedUser.getPhoneNumber() != null){
            user.setPhoneNumber(updatedUser.getPhoneNumber()); 
        }
        if (updatedUser.getAccountStatus() != null){
            user.setAccountStatus(updatedUser.getAccountStatus()); 
        }
        return userRepository.save(user);
    }

    public boolean verifyIdentity(Integer userId, String firstName, String lastName, String rawPassword){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return false;
        
        User user = optionalUser.get();
        return user.getFirstName().equals(firstName) &&
               user.getLastName().equals(lastName) &&
               passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }
    
    public User viewUser(Integer id){ 
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
    }
    
    public void logout(Integer userId) {
        if (!userRepository.existsById(userId)) {
             throw new RuntimeException("User not found.");
        }
        // Additional logout logic (e.g., JWT invalidation) goes here.
    }
}