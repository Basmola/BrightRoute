package com.brightroute.brightroute.dto;
import com.brightroute.brightroute.model.CourseSubscription;
import java.time.LocalDateTime;

public class CourseSubscriptionDTO {
    private Integer id;
    private Integer userId;  
    private Integer courseId;  
    private LocalDateTime subscribedAt;

    public CourseSubscriptionDTO(CourseSubscription subscription) {
        this.id = subscription.getId();
         
        this.userId = subscription.getUser().getId(); 
        this.courseId = subscription.getCourse().getCourseId();
        this.subscribedAt = subscription.getSubscribedAt();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }
    public LocalDateTime getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }
}