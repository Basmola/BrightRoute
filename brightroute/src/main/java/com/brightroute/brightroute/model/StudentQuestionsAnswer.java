package com.brightroute.brightroute.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "student_questions_answers")
public class StudentQuestionsAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Answer cannot be blank")
    @Size(max = 1000, message = "Answer must not exceed 1000 characters")
    private String answerText;

    @NotNull(message = "Correctness flag is required")
    private Boolean isCorrect;

    // Constructors
    public StudentQuestionsAnswer() {}

    public StudentQuestionsAnswer(Long studentId, Long questionId, String answerText, Boolean isCorrect) {
        this.studentId = studentId;
        this.questionId = questionId;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}