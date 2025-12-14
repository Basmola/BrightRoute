package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "QuizQuestion", schema = "quiz")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;  

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)  
    private Quiz quiz; 

    @Lob
    @Column(name = "question_text")
    private String questionText;

    @Lob
    @Column(name = "question_image")
    private byte[] questionImage;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestionsChoice> choices;

    public QuizQuestion() {}

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
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

    public List<QuestionsChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionsChoice> choices) {
        this.choices = choices;
    }
}