package com.brightroute.brightroute.model;

import jakarta.persistence.*;
// Removed unnecessary validation imports (should be done on DTOs/Service layer)

@Entity
// CORRECTION: Corrected schema and table name to match SQL
@Table(name = "StudentQuestionsAnswer", schema = "quiz")
public class StudentQuestionsAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer id; // CORRECTION: Changed from Long to Integer

    // CORRECTION: Many Answers belong to One Submission
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private StudentQuizSubmission submission;

    // CORRECTION: Many Answers belong to One Question
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    // CORRECTION: Many Answers select One Choice
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id", nullable = false)
    private QuestionsChoice choice;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    public StudentQuestionsAnswer() {}

    // Constructor updated for entity relationships (assuming entities are passed)
    public StudentQuestionsAnswer(StudentQuizSubmission submission, QuizQuestion question, QuestionsChoice choice, Boolean isCorrect) {
        this.submission = submission;
        this.question = question;
        this.choice = choice;
        this.isCorrect = isCorrect;
    }

    // ===== Getters and Setters =====
    
    public Integer getId() { return id; }
    // Setter for ID is usually omitted if generated

    // Getters and Setters for entity relationships
    public StudentQuizSubmission getSubmission() { return submission; }
    public void setSubmission(StudentQuizSubmission submission) { this.submission = submission; }

    public QuizQuestion getQuestion() { return question; }
    public void setQuestion(QuizQuestion question) { this.question = question; }

    public QuestionsChoice getChoice() { return choice; }
    public void setChoice(QuestionsChoice choice) { this.choice = choice; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}
