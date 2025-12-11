package com.brightroute.brightroute.model;

import com.brightroute.brightroute.enums.AccountStatus;
import com.brightroute.brightroute.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Users", schema = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id; 

    @Column(name = "user_first_name", nullable = false)
    private String firstName;

    @Column(name = "user_last_name", nullable = false)
    private String lastName;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;
    
    // ... (Other fields remain the same) ...

    @Column(name = "user_phone_number", nullable = false)
    private String phoneNumber; 

    @Column(name = "user_password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; 

    @Column(name = "user_account_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Lob
    @Column(name = "user_image")
    private byte[] userImage;

    @Column(name = "user_created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ----------------------------
    // RELATIONSHIPS
    // ----------------------------

    // 1. One-to-One: Inverse side of the Student profile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Student studentProfile;

    // 2. One-to-Many: Subscriptions
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CourseSubscription> subscriptions;

    // 3. One-to-Many: Quiz Submissions
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentQuizSubmission> quizSubmissions;

    // 4. One-to-Many: Enrollments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    // 5. One-to-Many: System Logs
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SystemLog> logs;
    
    // 6. One-to-Many: Access Codes used by this user
    @OneToMany(mappedBy = "usedBy", fetch = FetchType.LAZY)
    private List<AccessCode> usedAccessCodes;


    public User() {}

    // ===== Getters & Setters (FIXED: Added missing core methods) =====
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    // --- ADDED MISSING ---
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    // --- END ADDED MISSING ---
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Role getRole() { return role; } 
    public void setRole(Role role) { this.role = role; } 

    public AccountStatus getAccountStatus() { return accountStatus; }
    public void setAccountStatus(AccountStatus accountStatus) { this.accountStatus = accountStatus; } 

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // ... (Relation Getters/Setters remain)
}