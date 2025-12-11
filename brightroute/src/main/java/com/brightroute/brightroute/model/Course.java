package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Course", schema = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_title", nullable = false, length = 200)
    private String courseTitle;

    @Lob
    @Column(name = "course_description", nullable = false)
    private String courseDescription;

    @Lob
    @Column(name = "course_image_cover")
    private byte[] courseImageCover;

    @Column(name = "course_instructor", nullable = false, length = 150)
    private String courseInstructor;

    @Column(name = "course_number_of_lectures", nullable = false)
    private Integer courseNumberOfLectures;

    @Column(name = "course_created_at")
    private LocalDateTime courseCreatedAt;

    // Constructors, Getters, and Setters (omitted for brevity)
    // You should generate these, typically using Lombok or an IDE.

    // Example of a minimal constructor
    public Course() {
        this.courseNumberOfLectures = 0;
        this.courseCreatedAt = LocalDateTime.now();
    }

    // Getters and Setters for all fields
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public byte[] getCourseImageCover() {
        return courseImageCover;
    }

    public void setCourseImageCover(byte[] courseImageCover) {
        this.courseImageCover = courseImageCover;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public Integer getCourseNumberOfLectures() {
        return courseNumberOfLectures;
    }

    public void setCourseNumberOfLectures(Integer courseNumberOfLectures) {
        this.courseNumberOfLectures = courseNumberOfLectures;
    }

    public LocalDateTime getCourseCreatedAt() {
        return courseCreatedAt;
    }

    public void setCourseCreatedAt(LocalDateTime courseCreatedAt) {
        this.courseCreatedAt = courseCreatedAt;
    }
}