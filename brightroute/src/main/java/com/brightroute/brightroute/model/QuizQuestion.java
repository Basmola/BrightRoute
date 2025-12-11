package com.brightroute.brightroute.model;

import jakarta.persistence.*;

@Entity
@Table(name = "QuizQuestion", schema = "quiz")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    // This field links to the lectures.LecturePart table's part_id
    // For simplicity, we use an Integer for the FK, assuming LecturePart entity is not strictly needed here.
    @Column(name = "part_id", nullable = false)
    private Integer partId;

    @Lob
    @Column(name = "question_text")
    private String questionText;

    @Lob
    @Column(name = "question_image")
    private byte[] questionImage;

    // Constructors, Getters, and Setters (omitted for brevity)

    public QuizQuestion() {}

    // Getters and Setters for all fields
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public byte[] getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(byte[] questionImage) {
        this.questionImage = questionImage;
    }
}