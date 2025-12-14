package com.brightroute.brightroute.model;

import jakarta.persistence.*;

@Entity
 
@Table(name = "QuestionsChoice", schema = "quiz")
 
public class QuestionsChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Integer choiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

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

    public QuestionsChoice() {}

    public Integer getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Integer choiceId) {
        this.choiceId = choiceId;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
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