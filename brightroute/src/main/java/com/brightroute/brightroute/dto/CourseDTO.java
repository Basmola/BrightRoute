package com.brightroute.brightroute.dto;

public class CourseDTO {
    private Integer courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseInstructor;
    private Integer levelId;
    private byte[] courseImageCover;

    // Constructors
    public CourseDTO() {
    }

    public CourseDTO(Integer courseId, String courseTitle, String courseDescription,
            String courseInstructor, Integer levelId, byte[] courseImageCover) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseInstructor = courseInstructor;
        this.levelId = levelId;
        this.courseImageCover = courseImageCover;
    }

    // Getters and Setters
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

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public byte[] getCourseImageCover() {
        return courseImageCover;
    }

    public void setCourseImageCover(byte[] courseImageCover) {
        this.courseImageCover = courseImageCover;
    }
}
