package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AccessCode", schema = "access")
public class AccessCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Integer codeId; // CORRECTION: Changed from Long to Integer

    @Column(name = "code_value", nullable = false, unique = true, length = 200)
    private String codeValue;

    // CORRECTION: Mapped to the exact SQL column name
    @Column(name = "code_is_used", nullable = false)
    private Boolean codeIsUsed = false; // Using Boolean for better null handling/mapping with BIT

    // CORRECTION: Mapped to the exact SQL column name
    @Column(name = "code_used_at")
    private LocalDateTime codeUsedAt;

    // CORRECTION: Use updatable=false and LocalDateTime.now() for application-side default.
    // Removed insertable=false to allow JPA to insert the field (relying on DB default is also an option).
    @Column(name = "code_created_at", nullable = false, updatable = false)
    private LocalDateTime codeCreatedAt = LocalDateTime.now();

    @Column(name = "code_expires_at")
    private LocalDateTime codeExpiresAt; // ADDED: Uncommented and made a proper field

    // Relations (All should use FetchType.LAZY for performance)
    
    // Links to the Course for which the code was generated
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // ADDED: Links to the User who consumed the code (used_by FK in SQL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_by") // Field used_by is in the SQL table
    private User usedBy;

    // Links to the specific Lecture (if code grants access to a single lecture)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "used_for_lecture")
    private Lecture lecture;

    public AccessCode() {}

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

    // CORRECTION: Renamed getter/setter to match field name
    public Boolean getCodeIsUsed() {
        return codeIsUsed;
    }
    public void setCodeIsUsed(Boolean codeIsUsed) {
        this.codeIsUsed = codeIsUsed;
    }

    // CORRECTION: Renamed getter/setter to match field name
    public LocalDateTime getCodeUsedAt() {
        return codeUsedAt;
    }
    public void setCodeUsedAt(LocalDateTime codeUsedAt) {
        this.codeUsedAt = codeUsedAt;
    }

    // CORRECTION: Renamed getter/setter to match field name
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

    // ADDED: Getter/Setter for usedBy User
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