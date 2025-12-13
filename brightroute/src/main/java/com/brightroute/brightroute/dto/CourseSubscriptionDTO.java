package com.brightroute.brightroute.dto;
import com.brightroute.brightroute.model.CourseSubscription;
import java.time.LocalDateTime;

public class CourseSubscriptionDTO {
    private Integer id;
    private Integer userId; // Only the ID of the User
    private Integer courseId; // Only the ID of the Course
    private LocalDateTime subscribedAt;

    // Constructor to map the Entity to the DTO
    public CourseSubscriptionDTO(CourseSubscription subscription) {
        this.id = subscription.getId();
        // Extracting IDs from the related entities
        this.userId = subscription.getUser().getId(); 
        this.courseId = subscription.getCourse().getCourseId();
        this.subscribedAt = subscription.getSubscribedAt();
    }

    // Standard Getters and Setters (omitted for brevity)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public LocalDateTime getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }
}