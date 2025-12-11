package com.brightroute.brightroute.model;

import jakarta.persistence.*;

@Entity
@Table(name = "LecturePart", schema = "lectures")
public class LecturePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private Integer id; // CORRECTION: Changed from Long to Integer

    @Column(name = "part_type", nullable = false)
    private String partType; // 'QUIZ','VIDEO','PDF','TEXT'

    @Column(name = "part_content_url")
    private String partContentUrl; // content as URL

    @Column(name = "part_description")
    private String partDescription;

    @Column(name = "part_order_number", nullable = false)
    private Integer partOrderNumber;

    // Many LectureParts belong to One Lecture (Owning side)
    @ManyToOne(fetch = FetchType.LAZY) // ENHANCEMENT: Added fetch type
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;
    
    // One LecturePart has One Quiz (Inverse side of the One-to-One relationship)
    @OneToOne(mappedBy = "lecturePart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Quiz quiz; // ADDED: Inverse mapping to Quiz

    // Constructors
    public LecturePart() {}

    // ===== Getters & Setters =====
    public Integer getId() { return id; } // CORRECTION: Integer
    public void setId(Integer id) { this.id = id; } // CORRECTION: Integer

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
    
    public Quiz getQuiz() { return quiz; } // Getter for Quiz
    public void setQuiz(Quiz quiz) { this.quiz = quiz; } // Setter for Quiz
}