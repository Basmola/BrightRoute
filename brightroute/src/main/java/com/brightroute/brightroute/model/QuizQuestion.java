package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "QuizQuestion", schema = "quiz")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId; // Matches SQL INT type

    // CORRECTION: This links to the parent entity Quiz, not LecturePart ID directly.
    // Many QuizQuestions belong to One Quiz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false) // FK column name in SQL is 'quiz_id'
    private Quiz quiz; 
    
    // The original partId field is removed as the quiz ID is derived from the 'quiz' entity above.
    
    @Lob
    @Column(name = "question_text")
    private String questionText;

    @Lob
    @Column(name = "question_image")
    private byte[] questionImage;

    // ADDED: One QuizQuestion has Many Choices (Renamed QuestionsChoice in SQL)
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuestionsChoice> choices;


    // Constructors
    public QuizQuestion() {}

    // ===== Getters and Setters =====
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    // New getter/setter for the Quiz entity relationship
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

    // New getter/setter for the choices list
    public List<QuestionsChoice> getChoices() {
        return choices;
    }

    public void setChoices(List<QuestionsChoice> choices) {
        this.choices = choices;
    }
}