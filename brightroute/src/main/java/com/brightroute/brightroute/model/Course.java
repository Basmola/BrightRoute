package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private Integer courseNumberOfLectures = 0; // Default matches SQL

    // ADDED: Missing field from SQL schema
    @Column(name = "level_id")
    private Integer levelId;

    // ENHANCEMENT: Added updatable=false to respect DB default
    @Column(name = "course_created_at", updatable = false)
    private LocalDateTime courseCreatedAt = LocalDateTime.now();


    // ----------------------------
    // RELATIONSHIPS (INVERSE SIDE)
    // ----------------------------
    
    // One Course has Many Lectures
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Lecture> lectures;

    // One Course has Many Subscriptions
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CourseSubscription> subscriptions;

    // One Course can have Many Access Codes generated for it
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AccessCode> accessCodes;


    // Constructors
    public Course() {
        // Initializes fields with default values
        this.courseNumberOfLectures = 0;
        this.courseCreatedAt = LocalDateTime.now();
    }
    
    // ===== Getters and Setters =====
    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public String getCourseDescription() { return courseDescription; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }

    public byte[] getCourseImageCover() { return courseImageCover; }
    public void setCourseImageCover(byte[] courseImageCover) { this.courseImageCover = courseImageCover; }

    public String getCourseInstructor() { return courseInstructor; }
    public void setCourseInstructor(String courseInstructor) { this.courseInstructor = courseInstructor; }

    public Integer getCourseNumberOfLectures() { return courseNumberOfLectures; }
    public void setCourseNumberOfLectures(Integer courseNumberOfLectures) { this.courseNumberOfLectures = courseNumberOfLectures; }

    // Getter/Setter for levelId
    public Integer getLevelId() { return levelId; }
    public void setLevelId(Integer levelId) { this.levelId = levelId; }

    public LocalDateTime getCourseCreatedAt() { return courseCreatedAt; }
    public void setCourseCreatedAt(LocalDateTime courseCreatedAt) { this.courseCreatedAt = courseCreatedAt; }
    
    // Getters/Setters for new relationship lists
    public List<Lecture> getLectures() { return lectures; }
    public void setLectures(List<Lecture> lectures) { this.lectures = lectures; }

    public List<CourseSubscription> getSubscriptions() { return subscriptions; }
    public void setSubscriptions(List<CourseSubscription> subscriptions) { this.subscriptions = subscriptions; }

    public List<AccessCode> getAccessCodes() { return accessCodes; }
    public void setAccessCodes(List<AccessCode> accessCodes) { this.accessCodes = accessCodes; }
}