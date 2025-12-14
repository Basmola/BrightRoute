package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;

@Entity
@Table(name = "LecturePart", schema = "lectures")
public class LecturePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private Integer id;

    @Column(name = "part_type", nullable = false)
    private String partType;

    @Column(name = "part_content_url")
    private String partContentUrl;

    @Column(name = "part_content_text", columnDefinition = "TEXT")
    private String partContentText;

    @Column(name = "part_description")
    private String partDescription;

    @Column(name = "part_order_number", nullable = false)
    private Integer partOrderNumber;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    public LecturePart() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getPartContentUrl() {
        return partContentUrl;
    }

    public void setPartContentUrl(String partContentUrl) {
        this.partContentUrl = partContentUrl;
    }

    public String getPartContentText() {
        return partContentText;
    }

    public void setPartContentText(String partContentText) {
        this.partContentText = partContentText;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
    }

    public Integer getPartOrderNumber() {
        return partOrderNumber;
    }

    public void setPartOrderNumber(Integer partOrderNumber) {
        this.partOrderNumber = partOrderNumber;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    @JsonGetter("lectureId")
    public Integer getLectureId() {
        return lecture != null ? lecture.getId() : null;
    }
}