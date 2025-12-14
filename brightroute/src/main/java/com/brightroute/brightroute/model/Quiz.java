package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name = "Quiz", schema = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Integer id;  

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", unique = true, nullable = false)  
    private LecturePart lecturePart;

    @Column(name = "quiz_title")
    private String quizTitle;

    @Column(name = "quiz_passing_score")
    private Integer quizPassingScore;  

    @Column(name = "quiz_created_at", updatable = false)
    private LocalDateTime quizCreatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<QuizQuestion> questions;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentQuizSubmission> submissions;

    public Quiz() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LecturePart getLecturePart() { return lecturePart; }
    public void setLecturePart(LecturePart lecturePart) { this.lecturePart = lecturePart; }

    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }

    public Integer getQuizPassingScore() { return quizPassingScore; }
    public void setQuizPassingScore(Integer quizPassingScore) { this.quizPassingScore = quizPassingScore; }

    public LocalDateTime getQuizCreatedAt() { return quizCreatedAt; }
    public void setQuizCreatedAt(LocalDateTime quizCreatedAt) { this.quizCreatedAt = quizCreatedAt; }

    public List<QuizQuestion> getQuestions() { return questions; }
    public void setQuestions(List<QuizQuestion> questions) { this.questions = questions; }

    public List<StudentQuizSubmission> getSubmissions() { return submissions; }
    public void setSubmissions(List<StudentQuizSubmission> submissions) { this.submissions = submissions; }
}