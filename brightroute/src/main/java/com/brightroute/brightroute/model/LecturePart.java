package com.brightroute.brightroute.model;

import jakarta.persistence.*;

@Entity
@Table(name = "LecturePart", schema = "lectures")
public class LecturePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private Long id;

    @Column(name = "part_type", nullable = false)
    private String partType; // 'QUIZ','VIDEO','PDF','TEXT'

    @Column(name = "part_content_url")
    private String partContentUrl; // content as URL

    @Column(name = "part_description")
    private String partDescription;

    @Column(name = "part_order_number", nullable = false)
    private Integer partOrderNumber;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPartType() { return partType; }
    public void setPartType(String partType) { this.partType = partType; }

    public String getPartContentUrl() { return partContentUrl; }
    public void setPartContentUrl(String partContentUrl) { this.partContentUrl = partContentUrl; }

    public String getPartDescription() { return partDescription; }
    public void setPartDescription(String partDescription) { this.partDescription = partDescription; }

    public Integer getPartOrderNumber() { return partOrderNumber; }
    public void setPartOrderNumber(Integer partOrderNumber) { this.partOrderNumber = partOrderNumber; }

    public Lecture getLecture() { return lecture; }
    public void setLecture(Lecture lecture) { this.lecture = lecture; }
}
