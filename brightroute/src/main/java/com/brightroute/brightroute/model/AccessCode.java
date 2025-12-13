package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity
@Table(name = "AccessCode", schema = "access")
public class AccessCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Integer codeId;

    @Column(name = "code_value", nullable = false, unique = true, length = 200)
    private String codeValue;

    @Column(name = "code_is_used", nullable = false)
    private Boolean codeIsUsed = false;

    @Column(name = "code_used_at")
    private LocalDateTime codeUsedAt;

    @Column(name = "code_created_at", nullable = false, updatable = false)
    private LocalDateTime codeCreatedAt = LocalDateTime.now();

    @Column(name = "code_expires_at")
    private LocalDateTime codeExpiresAt;

    // Relations

    // Links to the Course for which the code was generated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties({ "lectures", "accessCodes", "hibernateLazyInitializer", "handler" })
    private Course course;

    // Links to the User who consumed the code
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_by")
    @JsonIgnoreProperties({ "usedAccessCodes", "subscriptions", "quizSubmissions", "enrollments", "logs",
            "studentProfile", "hibernateLazyInitializer", "handler" })
    private User usedBy;

    // Links to the specific Lecture
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_for_lecture")
    @JsonIgnoreProperties({ "course", "lectureParts", "hibernateLazyInitializer", "handler" })
    private Lecture lecture;

    public AccessCode() {
    }

    // ===== Getters and Setters =====

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public Boolean getCodeIsUsed() {
        return codeIsUsed;
    }

    public void setCodeIsUsed(Boolean codeIsUsed) {
        this.codeIsUsed = codeIsUsed;
    }

    public LocalDateTime getCodeUsedAt() {
        return codeUsedAt;
    }

    public void setCodeUsedAt(LocalDateTime codeUsedAt) {
        this.codeUsedAt = codeUsedAt;
    }

    public LocalDateTime getCodeCreatedAt() {
        return codeCreatedAt;
    }

    public void setCodeCreatedAt(LocalDateTime codeCreatedAt) {
        this.codeCreatedAt = codeCreatedAt;
    }

    public LocalDateTime getCodeExpiresAt() {
        return codeExpiresAt;
    }

    public void setCodeExpiresAt(LocalDateTime codeExpiresAt) {
        this.codeExpiresAt = codeExpiresAt;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(User usedBy) {
        this.usedBy = usedBy;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}