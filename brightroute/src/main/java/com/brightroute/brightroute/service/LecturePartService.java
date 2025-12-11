package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.repository.LecturePartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for best practice

import java.util.List;
import java.util.Optional;

@Service
public class LecturePartService {

    private final LecturePartRepository lecturePartRepository;

    public LecturePartService(LecturePartRepository lecturePartRepository) {
        this.lecturePartRepository = lecturePartRepository;
    }

    // ======================================================
    // 1. Core CRUD Methods (Required by LecturePartController)
    // ======================================================

    /**
     * Retrieves all lecture parts.
     */
    public List<LecturePart> getAllParts() {
        return lecturePartRepository.findAll();
    }

    /**
     * Finds a single lecture part by ID.
     */
    public Optional<LecturePart> findPartById(Integer partId) {
        return lecturePartRepository.findById(partId);
    }
    
    /**
     * Saves a LecturePart (used for creation or update/merge).
     */
    @Transactional
    public LecturePart savePart(LecturePart part) {
        // NOTE: Full creation logic (including setting the Lecture link) 
        // usually happens in LectureService's addPart method, 
        // but this method is needed for the Controller's PUT/update logic.
        return lecturePartRepository.save(part);
    }
    
    // ======================================================
    // 2. Content Management Methods (Existing Logic)
    // ======================================================

    /**
     * Helper method to find a LecturePart or throw an exception.
     */
    private LecturePart getPartByIdOrThrow(Integer partId) {
        return lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found for ID: " + partId));
    }

    /**
     * Retrieves the content (URL or Quiz object) for a specific lecture part.
     */
    public Object getContent(Integer partId) {
        LecturePart part = getPartByIdOrThrow(partId);
        
        // Logic to determine if it's a QUIZ (return Quiz object) vs. media (return URL)
        if ("QUIZ".equals(part.getPartType())) {
             // Fetch the linked Quiz entity
             return part.getQuiz(); 
        }
        
        return part.getPartContentUrl(); 
    }

    /**
     * Updates the content URL for a specific lecture part.
     */
    @Transactional
    public void updateContent(Integer partId, String newContentUrl) {
        LecturePart part = getPartByIdOrThrow(partId);
        
        if ("QUIZ".equals(part.getPartType())) {
             throw new IllegalArgumentException("Cannot update content URL for a QUIZ part. Update Quiz entity instead.");
        }
        
        part.setPartContentUrl(newContentUrl);
        lecturePartRepository.save(part);
    }
}