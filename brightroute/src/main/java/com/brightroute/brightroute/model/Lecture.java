package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "Lecture", schema = "courses")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long id;

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

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseController course;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LecturePart> parts;

    // ===== Getters and Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public CourseController getCourse() { return course; }
    public void setCourse(CourseController course) { this.course = course; }

    public List<LecturePart> getParts() { return parts; }
    public void setParts(List<LecturePart> parts) { this.parts = parts; }
}
