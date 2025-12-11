package com.brightroute.brightroute.model;

import jakarta.persistence.*;

@Entity
@Table(name = "QuizChoice", schema = "quiz")
public class QuizChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Integer choiceId;

    // This field links to the quiz.QuizQuestion table's question_id
    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @Lob
    @Column(name = "choice_text", nullable = false)
    private String choiceText;

    @Lob
    @Column(name = "choice_image")
    private byte[] choiceImage;

    @Column(name = "choice_is_correct", nullable = false)
    private Boolean choiceIsCorrect;

    @Lob
    @Column(name = "choice_explanation_text")
    private String choiceExplanationText;

    @Lob
    @Column(name = "choice_explanation_image")
    private byte[] choiceExplanationImage;

    // Constructors, Getters, and Setters (omitted for brevity)

    public QuizChoice() {}

    // Getters and Setters for all fields
    public Integer getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Integer choiceId) {
        this.choiceId = choiceId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public byte[] getChoiceImage() {
        return choiceImage;
    }

    public void setChoiceImage(byte[] choiceImage) {
        this.choiceImage = choiceImage;
    }

    public Boolean getChoiceIsCorrect() {
        return choiceIsCorrect;
    }

    public void setChoiceIsCorrect(Boolean choiceIsCorrect) {
        this.choiceIsCorrect = choiceIsCorrect;
    }

    public String getChoiceExplanationText() {
        return choiceExplanationText;
    }

    public void setChoiceExplanationText(String choiceExplanationText) {
        this.choiceExplanationText = choiceExplanationText;
    }

    public byte[] getChoiceExplanationImage() {
        return choiceExplanationImage;
    }

    public void setChoiceExplanationImage(byte[] choiceExplanationImage) {
        this.choiceExplanationImage = choiceExplanationImage;
    }
}