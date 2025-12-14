package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty; // Import added
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "Lecture", schema = "courses")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Integer id;

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

    // FIXED: Changed @JsonIgnore to @JsonProperty(access = WRITE_ONLY)
    // This allows you to send the course ID in the JSON when creating a lecture
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @JsonManagedReference
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LecturePart> parts;

    // Constructors
    public Lecture() {
    }

    // ===== Getters and Setters =====
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public void setLectureTitle(String lectureTitle) {
        this.lectureTitle = lectureTitle;
    }

    public String getLectureDescription() {
        return lectureDescription;
    }

    public void setLectureDescription(String lectureDescription) {
        this.lectureDescription = lectureDescription;
    }

    public byte[] getLectureImage() {
        return lectureImage;
    }

    public void setLectureImage(byte[] lectureImage) {
        this.lectureImage = lectureImage;
    }

    public Integer getLectureOrderNumber() {
        return lectureOrderNumber;
    }

    public void setLectureOrderNumber(Integer lectureOrderNumber) {
        this.lectureOrderNumber = lectureOrderNumber;
    }

    public LocalDateTime getLectureCreatedAt() {
        return lectureCreatedAt;
    }

    public void setLectureCreatedAt(LocalDateTime lectureCreatedAt) {
        this.lectureCreatedAt = lectureCreatedAt;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Convenience Getter for the Course ID
    // Note: Ensure your Course class has the method getCourseId() or getId()
    public Integer getCourseId() {
        return course != null ? course.getCourseId() : null;
    }

    public List<LecturePart> getParts() {
        return parts;
    }

    public void setParts(List<LecturePart> parts) {
        this.parts = parts;
    }
}