package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;  

@Entity
 
@Table(name = "Enrollment", schema = "access")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Integer enrollmentId;  

    @Column(name = "date_enrolled", updatable = false)
    private LocalDateTime dateEnrolled = LocalDateTime.now(); 

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;  

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  
    private User user;  

    public Enrollment() {}

    public Enrollment(Lecture lecture, User user) {
        this.lecture = lecture;
        this.user = user;
        this.dateEnrolled = LocalDateTime.now();
        this.status = EnrollmentStatus.ACTIVE;
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}