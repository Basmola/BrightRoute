package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Users", schema = "users")  // correct SQL table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_first_name")
    private String firstName;

    @Column(name = "user_last_name")
    private String lastName;

    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name = "user_phone_number")
    private Long phoneNumber;

    @Column(name = "user_password_hash")
    private String passwordHash;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private String role;   // ADMIN / STUDENT

    @Column(name = "user_account_status")
    private String accountStatus;  // ACTIVE / SUSPENDED

    @Lob
    @Column(name = "user_image")
    private byte[] userImage;   // matches VARBINARY(MAX)

    @Column(name = "user_created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ----------------------------
    // RELATIONSHIPS
    // ----------------------------

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student studentProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CourseSubscription> subscriptions;

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    // private List<LectureAccess> lectureAccess;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<StudentQuizSubmission> quizSubmissions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SystemLog> logs;

    public User() {}

    // Getters & Setters exactly like yours
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Long getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(Long phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }
    public byte[] getUserImage() { return userImage; }
    public void setUserImage(byte[] userImage) { this.userImage = userImage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
