package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "student_quiz_submissions")
public class StudentQuizSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    @Positive(message = "Score must be positive")
    private Double score;

    @NotNull(message = "Submission time is required")
    private LocalDateTime submittedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "submission_id")
    private List<StudentQuestionsAnswer> answers;

    // Constructors
    public StudentQuizSubmission() {}

    public StudentQuizSubmission(Long studentId, Long quizId, Double score, LocalDateTime submittedAt) {
        this.studentId = studentId;
        this.quizId = quizId;
        this.score = score;
        this.submittedAt = submittedAt;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public List<StudentQuestionsAnswer> getAnswers() { return answers; }
    public void setAnswers(List<StudentQuestionsAnswer> answers) { this.answers = answers; }
}