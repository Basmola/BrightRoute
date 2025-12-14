package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.QuestionsChoice;  
import com.brightroute.brightroute.service.QuestionsChoiceService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions-choices")  
public class QuestionsChoiceController {

    private final QuestionsChoiceService questionsChoiceService;  

    @Autowired
    public QuestionsChoiceController(QuestionsChoiceService questionsChoiceService) {
        this.questionsChoiceService = questionsChoiceService;
    }

    @GetMapping
    public List<QuestionsChoice> getAllChoices() {
        return questionsChoiceService.findAllChoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionsChoice> getChoiceById(@PathVariable Integer id) {
        return questionsChoiceService.findChoiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public QuestionsChoice createChoice(@RequestBody QuestionsChoice choice) {  
        return questionsChoiceService.saveChoice(choice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionsChoice> updateChoice(
            @PathVariable Integer id, 
            @RequestBody QuestionsChoice choiceDetails) {
        
        return questionsChoiceService.findChoiceById(id)
                .map(existingChoice -> {
                    choiceDetails.setChoiceId(id); 
                    QuestionsChoice updatedChoice = questionsChoiceService.saveChoice(choiceDetails);
                    return ResponseEntity.ok(updatedChoice);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChoice(@PathVariable Integer id) {
        if (questionsChoiceService.findChoiceById(id).isPresent()) {
            questionsChoiceService.deleteChoice(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}