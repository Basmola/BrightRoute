package com.brightroute.brightroute.dto;

import com.brightroute.brightroute.model.EnrollmentStatus;
import java.time.LocalDateTime;

public class EnrollmentDTO {
    private Integer enrollmentId;
    private Integer lectureId;
    private Integer userId;
    private EnrollmentStatus status;
    private LocalDateTime dateEnrolled;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Integer enrollmentId, Integer lectureId, Integer userId, EnrollmentStatus status,
            LocalDateTime dateEnrolled) {
        this.enrollmentId = enrollmentId;
        this.lectureId = lectureId;
        this.userId = userId;
        this.status = status;
        this.dateEnrolled = dateEnrolled;
    }

    public Integer getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Integer enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateEnrolled() {
        return dateEnrolled;
    }

    public void setDateEnrolled(LocalDateTime dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }
}
