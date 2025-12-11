package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
// CORRECTION: Added schema and correct table name (using schema.Table format)
@Table(name = "CourseSubscription", schema = "courses")
public class CourseSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // CORRECTION: Changed from Long to Integer

    // CORRECTION: Link to the User entity via the correct column name (user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Links to User, not Student

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // CORRECTION: Correct column name is subscription_subscribed_at
    @Column(name = "subscription_subscribed_at", nullable = false, updatable = false)
    private LocalDateTime subscribedAt;

    // Constructors
    public CourseSubscription() {}

    // Constructor updated to use User instead of Student
    public CourseSubscription(User user, Course course) {
        this.user = user;
        this.course = course;
        this.subscribedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() { // CORRECTION: Renamed getStudent to getUser
        return user;
    }

    public void setUser(User user) { // CORRECTION: Renamed setStudent to setUser
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getSubscribedAt() {
        return subscribedAt;
    }

    public void setSubscribedAt(LocalDateTime subscribedAt) {
        this.subscribedAt = subscribedAt;
    }
}