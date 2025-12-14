package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;  
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Course", schema = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @NotBlank(message = "Course title is mandatory")
    @Size(max = 200, message = "Title length must be <= 200")
    @Column(name = "course_title", nullable = false, length = 200)
    private String courseTitle;

    @NotBlank(message = "Course description is mandatory")
    @Lob
    @Column(name = "course_description", nullable = false)
    private String courseDescription;

    @Lob
    @Column(name = "course_image_cover")
    private byte[] courseImageCover;

    @NotBlank(message = "Instructor is mandatory")
    @Size(max = 150, message = "Instructor name must be <= 150")
    @Column(name = "course_instructor", nullable = false, length = 150)
    private String courseInstructor;

    @Min(value = 0, message = "Lectures count cannot be negative")
    @Column(name = "course_number_of_lectures", nullable = false)
    private Integer courseNumberOfLectures = 0;

    @Column(name = "level_id")
    private Integer levelId;

    @Column(name = "course_created_at", updatable = false)
    private LocalDateTime courseCreatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Lecture> lectures;

    public Course() {
        this.courseNumberOfLectures = 0;
        this.courseCreatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void syncLecturesCount() {
        if (this.lectures != null) {
            this.courseNumberOfLectures = this.lectures.size();
        } else {
            this.courseNumberOfLectures = 0;
        }
    }

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

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public LocalDateTime getCourseCreatedAt() {
        return courseCreatedAt;
    }

    public void setCourseCreatedAt(LocalDateTime courseCreatedAt) {
        this.courseCreatedAt = courseCreatedAt;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}