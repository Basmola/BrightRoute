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

    @Column(name = "part_content_text")
    private String partContentText;

    @Column(name = "part_description")
    private String partDescription;

    @Lob
    @Column(name = "part_content")
    private byte[] partContent;

    @Column(name = "part_order_number", nullable = false)
    private Integer partOrderNumber;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(name = "part_rating")
    private Double rating;

   // getter و setter
   public Double getRating() {
    return rating;
   }

   public void setRating(Double rating) {
    this.rating = rating;
   }


    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
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

    public byte[] getPartContent() {
        return partContent;
    }

    public void setPartContent(byte[] partContent) {
        this.partContent = partContent;
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
}

