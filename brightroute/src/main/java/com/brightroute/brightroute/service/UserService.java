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

    @Transactional
    public User registerUserAndStudent(UserStudentRegistrationDto dto) {

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

        studentProfile.setNationalId(dto.getNationalId());
        studentProfile.setParentNumber(dto.getParentNumber());
        studentProfile.setIdType(dto.getIdType());
        studentProfile.setLevelOfEducation(dto.getLevelOfEducation());
        studentProfile.setNationalIdFront(dto.getNationalIdFront());
        studentProfile.setBirthCertificate(dto.getBirthCertificate());

        studentProfile.setUser(user);

        User savedUser = userRepository.save(user);

        if (dto.getRole() == Role.STUDENT) {
            studentProfile.setUser(savedUser);  
            studentRepository.save(studentProfile);
        }

        systemLogService.logAction("REGISTRATION", savedUser.getId(),
                "User registered successfully with role: " + savedUser.getRole());

        return savedUser;
    }

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
            existingUser.setRole(updatedUser.getRole());  
        if (updatedUser.getAccountStatus() != null)
            existingUser.setAccountStatus(updatedUser.getAccountStatus());  

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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Integer id) {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
         
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found for ID: " + id);
        }

        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        }

        userRepository.deleteById(id);
    }

    public void logout(Integer userId) {
        System.out.println("User ID " + userId + " logged out.");
    }

    public UserStudentRegistrationDto getProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Student student = studentRepository.findById(userId).orElse(null);

        UserStudentRegistrationDto dto = new UserStudentRegistrationDto();

        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setRole(user.getRole());
        dto.setAccountStatus(user.getAccountStatus());
        dto.setUserImage(user.getUserImage());

        if (student != null) {
            dto.setNationalId(student.getNationalId());
            dto.setParentNumber(student.getParentNumber());
            dto.setIdType(student.getIdType());
            dto.setLevelOfEducation(student.getLevelOfEducation());
             
            dto.setNationalIdFront(student.getNationalIdFront());
            dto.setBirthCertificate(student.getBirthCertificate());
        }

        return dto;
    }

    @Transactional
    public User updateFullProfile(Integer userId, UserStudentRegistrationDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());
        if (dto.getPhoneNumber() != null)
            user.setPhoneNumber(dto.getPhoneNumber());

        User savedUser = userRepository.save(user);

        if (user.getRole() == Role.STUDENT) {
            Student student = studentRepository.findById(userId)
                    .orElse(new Student());  

            if (student.getUser() == null) {
                student.setUser(user);  
            }

            if (dto.getNationalId() != null)
                student.setNationalId(dto.getNationalId());
            if (dto.getParentNumber() != null)
                student.setParentNumber(dto.getParentNumber());
            if (dto.getIdType() != null)
                student.setIdType(dto.getIdType());
            if (dto.getLevelOfEducation() != null)
                student.setLevelOfEducation(dto.getLevelOfEducation());
             
            if (dto.getNationalIdFront() != null)
                student.setNationalIdFront(dto.getNationalIdFront());
            if (dto.getBirthCertificate() != null)
                student.setBirthCertificate(dto.getBirthCertificate());

            studentRepository.save(student);
        }

        return savedUser;
    }
}
