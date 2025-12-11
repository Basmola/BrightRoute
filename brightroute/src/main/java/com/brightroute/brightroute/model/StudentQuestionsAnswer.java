package com.brightroute.brightroute.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "student_quiz_answers")
public class StudentQuestionsAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @NotNull(message = "Submission ID is required")
    @Column(name = "submission_id")
    private Long submissionId;

    @NotNull(message = "Question ID is required")
    @Column(name = "question_id")
    private Long questionId;

    @NotNull(message = "Choice ID is required")
    @Column(name = "choice_id")
    private Long choiceId;

    @NotNull(message = "Correctness flag is required")
    @Column(name = "is_correct")
    private Boolean isCorrect;

    public StudentQuestionsAnswer() {}

    public StudentQuestionsAnswer(Long submissionId, Long questionId, Long choiceId, Boolean isCorrect) {
        this.submissionId = submissionId;
        this.questionId = questionId;
        this.choiceId = choiceId;
        this.isCorrect = isCorrect;
    }

    public Long getId() { return id; }

    public Long getSubmissionId() { return submissionId; }
    public void setSubmissionId(Long submissionId) { this.submissionId = submissionId; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public Long getChoiceId() { return choiceId; }
    public void setChoiceId(Long choiceId) { this.choiceId = choiceId; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}

// @Entity
// @Table(name = "student_questions_answers")
// public class StudentQuestionsAnswer {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @NotNull(message = "Student ID is required")
//     private Long studentId;

//     @NotNull(message = "Question ID is required")
//     private Long questionId;

//     @NotBlank(message = "Answer cannot be blank")
//     @Size(max = 1000, message = "Answer must not exceed 1000 characters")
//     private String answerText;

//     @NotNull(message = "Correctness flag is required")
//     private Boolean isCorrect;

//     // Constructors
//     public StudentQuestionsAnswer() {}

//     public StudentQuestionsAnswer(Long studentId, Long questionId, String answerText, Boolean isCorrect) {
//         this.studentId = studentId;
//         this.questionId = questionId;
//         this.answerText = answerText;
//         this.isCorrect = isCorrect;
//     }

//     // Getters & Setters
//     public Long getId() { return id; }
//     public Long getStudentId() { return studentId; }
//     public void setStudentId(Long studentId) { this.studentId = studentId; }

//     public Long getQuestionId() { return questionId; }
//     public void setQuestionId(Long questionId) { this.questionId = questionId; }

//     public String getAnswerText() { return answerText; }
//     public void setAnswerText(String answerText) { this.answerText = answerText; }

//     public Boolean getIsCorrect() { return isCorrect; }
//     public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
// }