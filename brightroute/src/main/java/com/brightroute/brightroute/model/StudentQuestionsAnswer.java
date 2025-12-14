package com.brightroute.brightroute.model;

import jakarta.persistence.*;

@Entity
 
@Table(name = "StudentQuestionsAnswer", schema = "quiz")
public class StudentQuestionsAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer id;  

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private StudentQuizSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id", nullable = false)
    private QuestionsChoice choice;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    public StudentQuestionsAnswer() {}

    public StudentQuestionsAnswer(StudentQuizSubmission submission, QuizQuestion question, QuestionsChoice choice, Boolean isCorrect) {
        this.submission = submission;
        this.question = question;
        this.choice = choice;
        this.isCorrect = isCorrect;
    }

    public Integer getId() { return id; }

    public StudentQuizSubmission getSubmission() { return submission; }
    public void setSubmission(StudentQuizSubmission submission) { this.submission = submission; }

    public QuizQuestion getQuestion() { return question; }
    public void setQuestion(QuizQuestion question) { this.question = question; }

    public QuestionsChoice getChoice() { return choice; }
    public void setChoice(QuestionsChoice choice) { this.choice = choice; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}
