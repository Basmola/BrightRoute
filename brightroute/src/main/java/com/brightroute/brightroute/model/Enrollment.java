package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime; // Use LocalDateTime for DATETIME SQL type

@Entity
// CORRECTION: Specify the schema and table name
@Table(name = "Enrollment", schema = "access")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Integer enrollmentId; // CORRECTION: Changed from int to Integer

    // CORRECTION: Field name must match SQL column name. Use LocalDateTime.
    @Column(name = "date_enrolled", updatable = false)
    private LocalDateTime dateEnrolled = LocalDateTime.now(); 

    // Field name must match SQL column name.
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE; // Use the enum

    // Removed the 'complete' field as it does not exist in the SQL table.

    // Many enrollments → one lecture
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    // CORRECTION: Many enrollments → one USER (FK is user_id in SQL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // SQL FK column is 'user_id'
    private User user; // Should link to the User entity

    public Enrollment() {}

    // Convenience constructor updated for corrected types and field names
    public Enrollment(Lecture lecture, User user) {
        this.lecture = lecture;
        this.user = user;
        this.dateEnrolled = LocalDateTime.now();
        this.status = EnrollmentStatus.ACTIVE;
    }

    // ------------------ Getters & Setters ------------------

    public Integer getEnrollmentId() {
        return enrollmentId;
    }
    // No setter for ID required if it's generated

    public LocalDateTime getDateEnrolled() {
        return dateEnrolled;
    }

    public void setDateEnrolled(LocalDateTime dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    // Getter/Setter for User entity (renamed from getStudent/setStudent)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}