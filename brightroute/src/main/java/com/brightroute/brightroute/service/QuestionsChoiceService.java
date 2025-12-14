package com.brightroute.brightroute.service;
import com.brightroute.brightroute.model.QuestionsChoice;  
import com.brightroute.brightroute.repository.QuestionsChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionsChoiceService {  

    private final QuestionsChoiceRepository questionsChoiceRepository;  

    @Autowired
    public QuestionsChoiceService(QuestionsChoiceRepository questionsChoiceRepository) {  
        this.questionsChoiceRepository = questionsChoiceRepository;
    }

    public List<QuestionsChoice> findAllChoices() {  
        return questionsChoiceRepository.findAll();
    }

    public Optional<QuestionsChoice> findChoiceById(Integer id) {  
        return questionsChoiceRepository.findById(id);
    }

    public QuestionsChoice saveChoice(QuestionsChoice choice) {  
        return questionsChoiceRepository.save(choice);
    }

    public void deleteChoice(Integer id) {
        questionsChoiceRepository.deleteById(id);
    }
}