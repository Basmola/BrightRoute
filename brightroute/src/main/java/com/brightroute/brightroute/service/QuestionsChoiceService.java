package com.brightroute.brightroute.service;
import com.brightroute.brightroute.model.QuestionsChoice; // Use the correct model name
import com.brightroute.brightroute.repository.QuestionsChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionsChoiceService { // CORRECTION: Renamed service class

    private final QuestionsChoiceRepository questionsChoiceRepository; // CORRECTION: Renamed repository field

    @Autowired
    public QuestionsChoiceService(QuestionsChoiceRepository questionsChoiceRepository) { // CORRECTION: Renamed constructor parameter
        this.questionsChoiceRepository = questionsChoiceRepository;
    }

    public List<QuestionsChoice> findAllChoices() { // CORRECTION: Use QuestionsChoice
        return questionsChoiceRepository.findAll();
    }

    public Optional<QuestionsChoice> findChoiceById(Integer id) { // CORRECTION: Use QuestionsChoice
        return questionsChoiceRepository.findById(id);
    }

    public QuestionsChoice saveChoice(QuestionsChoice choice) { // CORRECTION: Use QuestionsChoice
        return questionsChoiceRepository.save(choice);
    }

    public void deleteChoice(Integer id) {
        questionsChoiceRepository.deleteById(id);
    }
}