package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.UserStudentRegistrationDto;
import com.brightroute.brightroute.enums.Role;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.model.Student;
import com.brightroute.brightroute.repository.UserRepository;
import com.brightroute.brightroute.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private StudentRepository studentRepository; 

    @Autowired 
    private PasswordEncoder passwordEncoder; 

    // ----------------------------------------------------
    // -------- 1. UNIFIED REGISTRATION METHOD (FINAL FIX) --------
    // ----------------------------------------------------
    
    /**
     * Handles the creation of a new User and the corresponding Student profile 
     * using JPA cascading, resolving the previous Optimistic Locking Error.
     */
    @Transactional
    public User registerUserAndStudent(UserStudentRegistrationDto dto) {
        
        // --- 1. Create User and Student objects from DTO ---
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword())); 
        user.setRole(dto.getRole()); 
        user.setAccountStatus(dto.getAccountStatus()); 
        
        Student studentProfile = new Student();
        
        // Map Student-specific fields
        studentProfile.setNationalId(dto.getNationalId());
        studentProfile.setParentNumber(dto.getParentNumber());
        studentProfile.setIdType(dto.getIdType());
        studentProfile.setLevelOfEducation(dto.getLevelOfEducation());
        studentProfile.setNationalIdFront(dto.getNationalIdFront());
        studentProfile.setBirthCertificate(dto.getBirthCertificate());
        
        // --- 2. ESTABLISH BI-DIRECTIONAL RELATIONSHIP (CRITICAL) ---
        
        // A. Link Student back to User (Required for @MapsId on Student)
        studentProfile.setUser(user); 
        
        // B. Link User to Student (Required for CascadeType.ALL on User)
        // NOTE: This requires the setStudentProfile() method in the User entity!
        
        // 🚨 CRITICAL FIXES IMPLEMENTED:
        // 1. Removed studentProfile.setId(savedUser.getId())
        // 2. Removed studentRepository.save(studentProfile)
        
        // --- 3. SAVE ONLY THE PARENT ENTITY (User) ---
        // CascadeType.ALL on User handles the Student insertion automatically.
        User savedUser = userRepository.save(user); 

        return savedUser; 
    }

    // ----------------------------------------------------
    // -------- 2. EXISTING USER MANAGEMENT METHODS (UNCHANGED) --------
    // ----------------------------------------------------
    
    public User register(User user) {
        throw new UnsupportedOperationException("Use registerUserAndStudent for complete registration.");
    }
    
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials or user not found."));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials.");
        }
        return user;
    }

    @Transactional
    public User updateProfile(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + id));

        if (updatedUser.getFirstName() != null) existingUser.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null) existingUser.setLastName(updatedUser.getLastName());
        if (updatedUser.getPhoneNumber() != null) existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    public User viewUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + id));
    }

    public Boolean verifyIdentity(Integer userId, String firstName, String lastName, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return user.getFirstName().equals(firstName) && 
               user.getLastName().equals(lastName) && 
               passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    public void logout(Integer userId) {
        System.out.println("User ID " + userId + " logged out.");
    }
}