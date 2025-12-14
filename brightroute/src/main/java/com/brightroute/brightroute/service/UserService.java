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

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SystemLogService systemLogService;

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
        user.setUserImage(dto.getUserImage());

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
        //

        // 🚨 CRITICAL FIX RESTORED: Explicitly save student if role is STUDENT
        // Since we removed the bi-directional relationship from User side to avoid
        // recursion/locking issues,
        // we must manually save the Student entity here.
        if (dto.getRole() == Role.STUDENT) {
            studentProfile.setUser(savedUser); // Link to saved user (ID will be mapped)
            studentRepository.save(studentProfile);
        }

        // Log Registration
        systemLogService.logAction("REGISTRATION", savedUser.getId(),
                "User registered successfully with role: " + savedUser.getRole());

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
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + id));

        if (updatedUser.getFirstName() != null)
            existingUser.setFirstName(updatedUser.getFirstName());
        if (updatedUser.getLastName() != null)
            existingUser.setLastName(updatedUser.getLastName());
        if (updatedUser.getPhoneNumber() != null)
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        if (updatedUser.getRole() != null)
            existingUser.setRole(updatedUser.getRole()); // Allow role update
        if (updatedUser.getAccountStatus() != null)
            existingUser.setAccountStatus(updatedUser.getAccountStatus()); // Allow status update

        return userRepository.save(existingUser);
    }

    public User viewUser(Integer id) {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + id));
    }

    public Boolean verifyIdentity(Integer userId, String firstName, String lastName, String rawPassword) {
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return user.getFirstName().equals(firstName) &&
                user.getLastName().equals(lastName) &&
                passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    // NEW METHODS FOR USER MANAGEMENT
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Integer id) {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        // Check if user exists
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found for ID: " + id);
        }

        // Check if there is a student profile associated with this user
        // Since Student shares the PK with User, we can check by ID
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        }

        // Now delete the user
        userRepository.deleteById(id);
    }

    public void logout(Integer userId) {
        System.out.println("User ID " + userId + " logged out.");
    }

    // ----------------------------------------------------
    // -------- 3. FULL PROFILE MANAGEMENT (DTO BASED) ----
    // ----------------------------------------------------

    public UserStudentRegistrationDto getProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Try to find student profile (might not exist for ADMINs)
        Student student = studentRepository.findById(userId).orElse(null);

        UserStudentRegistrationDto dto = new UserStudentRegistrationDto();

        // Map User fields
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setRole(user.getRole());
        dto.setAccountStatus(user.getAccountStatus());
        dto.setUserImage(user.getUserImage());
        // Password is not sent back for security

        // Map Student fields if available
        if (student != null) {
            dto.setNationalId(student.getNationalId());
            dto.setParentNumber(student.getParentNumber());
            dto.setIdType(student.getIdType());
            dto.setLevelOfEducation(student.getLevelOfEducation());
            // Images are byte arrays, sending them might be heavy but requested
            dto.setNationalIdFront(student.getNationalIdFront());
            dto.setBirthCertificate(student.getBirthCertificate());
        }

        return dto;
    }

    @Transactional
    public User updateFullProfile(Integer userId, UserStudentRegistrationDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update User fields
        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());
        if (dto.getPhoneNumber() != null)
            user.setPhoneNumber(dto.getPhoneNumber());
        // Email usually shouldn't be changed or needs verification, skipping for now or
        // allow if needed
        // if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        User savedUser = userRepository.save(user);

        // Update Student fields if role is STUDENT
        if (user.getRole() == Role.STUDENT) {
            Student student = studentRepository.findById(userId)
                    .orElse(new Student()); // Should exist, but handle case

            if (student.getUser() == null) {
                student.setUser(user); // Ensure link if creating new (though ID should match)
            }

            if (dto.getNationalId() != null)
                student.setNationalId(dto.getNationalId());
            if (dto.getParentNumber() != null)
                student.setParentNumber(dto.getParentNumber());
            if (dto.getIdType() != null)
                student.setIdType(dto.getIdType());
            if (dto.getLevelOfEducation() != null)
                student.setLevelOfEducation(dto.getLevelOfEducation());
            // Handle images if provided
            if (dto.getNationalIdFront() != null)
                student.setNationalIdFront(dto.getNationalIdFront());
            if (dto.getBirthCertificate() != null)
                student.setBirthCertificate(dto.getBirthCertificate());

            studentRepository.save(student);
        }

        return savedUser;
    }
}
