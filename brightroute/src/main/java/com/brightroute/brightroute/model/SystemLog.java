package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SystemLog", schema = "logs")
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId; // CORRECTION: Changed from Long to Integer

    // ADDED: Nullable Many-to-One relationship to User
    // The user_id is nullable in SQL (ON DELETE SET NULL)
    // ADDED: Nullable Many-to-One relationship to User
    // The user_id is nullable in SQL (ON DELETE SET NULL)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "action", nullable = false, length = 255)
    private String action;

    @Lob // Using @Lob for potentially large details text
    @Column(name = "details")
    private String details;

    @Column(name = "timestamp", updatable = false)
    // NOTE: Relying on the SQL default (GETDATE()) for insertion
    private LocalDateTime timestamp;

    // Constructors
    public SystemLog() {
    }

    // ===== Getters and Setters =====

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
    // Setter for timestamp is usually omitted if it's database-managed
}

// public User getUser() {
// return user;
// }
// public void setUser(User user) {
// this.user = user;
// }
