package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

// CRITICAL CORRECTION: Import the correct Entity class, not the Controller
// import com.brightroute.brightroute.controller.CourseController; <--- REMOVED

@Entity
@Table(name = "Lecture", schema = "courses")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Integer id; // CORRECTION: Changed to Integer to match SQL INT type

    @Column(name = "lecture_title", nullable = false)
    private String lectureTitle;

    @Column(name = "lecture_description")
    private String lectureDescription;

    @Lob
    @Column(name = "lecture_image")
    private byte[] lectureImage;

    @Column(name = "lecture_order_number", nullable = false)
    private Integer lectureOrderNumber;

    @Column(name = "lecture_created_at", updatable = false)
    private LocalDateTime lectureCreatedAt = LocalDateTime.now();

    // CRITICAL CORRECTION: Changed type to the Lecture's parent Entity (Course)
    @ManyToOne(fetch = FetchType.LAZY) // Added FetchType.LAZY for performance
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Should link to the Course entity

    // Added FetchType.LAZY for collection loading optimization
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LecturePart> parts;

    // Constructors (recommended to include a no-arg constructor)
    public Lecture() {}

    // ===== Getters and Setters =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLectureTitle() { return lectureTitle; }
    public void setLectureTitle(String lectureTitle) { this.lectureTitle = lectureTitle; }

    public String getLectureDescription() { return lectureDescription; }
    public void setLectureDescription(String lectureDescription) { this.lectureDescription = lectureDescription; }

    public byte[] getLectureImage() { return lectureImage; }
    public void setLectureImage(byte[] lectureImage) { this.lectureImage = lectureImage; }

    public Integer getLectureOrderNumber() { return lectureOrderNumber; }
    public void setLectureOrderNumber(Integer lectureOrderNumber) { this.lectureOrderNumber = lectureOrderNumber; }

    public LocalDateTime getLectureCreatedAt() { return lectureCreatedAt; }
    public void setLectureCreatedAt(LocalDateTime lectureCreatedAt) { this.lectureCreatedAt = lectureCreatedAt; }

    // Updated getter/setter types
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public List<LecturePart> getParts() { return parts; }
    public void setParts(List<LecturePart> parts) { this.parts = parts; }
}