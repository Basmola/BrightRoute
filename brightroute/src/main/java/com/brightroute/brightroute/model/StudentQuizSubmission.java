package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
// Removed validation imports (validation should be done on DTOs/Service layer)

@Entity
@Table(name = "StudentQuizSubmission", schema = "quiz")
public class StudentQuizSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Integer id; // CORRECTION: Changed from Long to Integer

    // CORRECTION: Many Submissions belong to One User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // CORRECTION: Many Submissions belong to One Quiz (not LecturePart)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false) // SQL FK is 'quiz_id'
    private Quiz quiz;

    @Column(name = "submission_score")
    private Integer submissionScore; // Renamed for better convention

    @Column(name = "submission_is_passed")
    private Boolean isPassed;

    @Column(name = "submission_submitted_at", updatable = false) // ENHANCEMENT: Added updatable=false
    private LocalDateTime submittedAt = LocalDateTime.now();

    // One Submission has Many Answers
    // Mapped via the 'submission' field in the StudentQuestionsAnswer entity (preferred JPA style)
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentQuestionsAnswer> answers;

    // Constructors
    public StudentQuizSubmission() {}

    // ===== Getters & Setters =====
    
    public Integer getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public Integer getSubmissionScore() { return submissionScore; }
    public void setSubmissionScore(Integer submissionScore) { this.submissionScore = submissionScore; }

    public Boolean getIsPassed() { return isPassed; }
    public void setIsPassed(Boolean isPassed) { this.isPassed = isPassed; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public List<StudentQuestionsAnswer> getAnswers() { return answers; }
    public void setAnswers(List<StudentQuestionsAnswer> answers) { this.answers = answers; }
}


