package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.util.List;

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

    @Column(name = "lecture_is_open")
    private Boolean lectureIsOpen = true;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LecturePart> parts;

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Boolean getLectureIsOpen() {
        return lectureIsOpen;
    }

    public void setLectureIsOpen(Boolean lectureIsOpen) {
        this.lectureIsOpen = lectureIsOpen;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<LecturePart> getParts() {
        return parts;
    }

    public void setParts(List<LecturePart> parts) {
        this.parts = parts;
    }
}
