package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SystemLog", schema = "logs")
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "action", nullable = false, length = 255)
    private String action;

    @Column(name = "details")
    private String details;

    @Column(name = "timestamp", insertable = false, updatable = false)
    private LocalDateTime timestamp;

    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    // Getters and Setters
    public Long getLogId() {
        return logId;
    }
    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // public User getUser() {
    //     return user;
    // }
    // public void setUser(User user) {
    //     this.user = user;
    // }
}
