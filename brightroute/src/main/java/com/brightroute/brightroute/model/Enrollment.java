package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int enrollmentId;

    private LocalDate dateEnrolled;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private boolean complete;

    // Many enrollments → one lecture
    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    // Many enrollments → one student
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public Enrollment() {}

    public Enrollment(Lecture lecture, Student student) {
        this.lecture = lecture;
        this.student = student;
        this.dateEnrolled = LocalDate.now();
        this.status = EnrollmentStatus.ACTIVE;
        this.complete = false;
    }

    // ------------------ Getters & Setters ------------------

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public LocalDate getDateEnrolled() {
        return dateEnrolled;
    }

    public void setDateEnrolled(LocalDate dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
