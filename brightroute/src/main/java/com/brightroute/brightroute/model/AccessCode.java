package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AccessCode", schema = "access")
public class AccessCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long codeId;

    @Column(name = "code_value", nullable = false, unique = true, length = 200)
    private String codeValue;

    @Column(name = "code_is_used", nullable = false)
    private boolean isUsed = false;

    @Column(name = "code_used_at")
    private LocalDateTime usedAt;

    @Column(name = "code_created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;

    // لو محتاج تستخدمه بعدين
    // @Column(name = "code_expires_at")
    // private LocalDateTime expiryDate;

    // Relations
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // @ManyToOne
    // @JoinColumn(name = "used_by")
    // private User usedBy;

    @ManyToOne
    @JoinColumn(name = "used_for_lecture")
    private Lecture lecture;

    // Getters and Setters
    public Long getCodeId() {
        return codeId;
    }
    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public String getCodeValue() {
        return codeValue;
    }
    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public boolean isUsed() {
        return isUsed;
    }
    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }
    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    // public User getUsedBy() {
    //     return usedBy;
    // }
    // public void setUsedBy(User usedBy) {
    //     this.usedBy = usedBy;
    // }

    public Lecture getLecture() {
        return lecture;
    }
    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
