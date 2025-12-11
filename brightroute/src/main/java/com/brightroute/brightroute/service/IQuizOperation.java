package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Quiz;

public interface IQuizOperation {
    Quiz addQuiz(Quiz quiz);
    Quiz updateQuiz(Long quizId, Quiz updated);
    void deleteQuiz(Long quizId);
}
