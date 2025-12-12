package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter; // Optional, but good practice for renaming

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

    @Column(name = "part_description")
    private String partDescription;

    @Column(name = "part_order_number", nullable = false)
    private Integer partOrderNumber;

    // FIX: @JsonBackReference tells Jackson to stop here and not serialize the Lecture object
    // This assumes the Lecture entity uses @JsonManagedReference on its 'parts' field.
    @JsonBackReference 
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;
    
    // Constructors
    public LecturePart() {}

    // ===== Getters & Setters =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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

    // FIX: CONVENIENCE GETTER TO EXPOSE THE LECTURE ID
    /**
     * Exposes the foreign key (lecture_id) as a separate field in the JSON response.
     */
    @JsonGetter("lectureId")
    public Integer getLectureId() {
        return lecture != null ? lecture.getId() : null; 
    }
}