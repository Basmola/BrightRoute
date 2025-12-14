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
    // CascadeType.ALL is CRITICAL for saving the Student automatically during
    // registration
    // ----------------------------

    // Other MappedBy relationships
    // Other MappedBy relationships
    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CourseSubscription> subscriptions;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentQuizSubmission> quizSubmissions;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SystemLog> logs;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "usedBy", fetch = FetchType.LAZY)
    private List<AccessCode> usedAccessCodes;

    public User() {
    }

    // ===== Getters & Setters =====

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    // Add Getter/Setter for userImage if needed
    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
