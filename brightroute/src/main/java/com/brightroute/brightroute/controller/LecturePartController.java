package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.service.LecturePartService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/lecture-parts") 
public class LecturePartController {

    private final LecturePartService lecturePartService;

    @Autowired
    public LecturePartController(LecturePartService lecturePartService) {
        this.lecturePartService = lecturePartService;
    }

    // ----------------------------------------------------
    // Core CRUD Operations
    // ----------------------------------------------------

    @GetMapping
    public List<LecturePart> getAllParts() {
        return lecturePartService.getAllParts();
    }
    
    @GetMapping("/{partId}")
    public ResponseEntity<LecturePart> getPartById(@PathVariable Integer partId) {
        return lecturePartService.findPartById(partId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{partId}")
    public ResponseEntity<LecturePart> updatePart(
            @PathVariable Integer partId,
            @RequestBody LecturePart updatedPart) {
        
        return lecturePartService.findPartById(partId)
                .map(existingPart -> {
                    updatedPart.setId(partId); // Ensure the ID from the path is used
                    
                    // CRITICAL FIX: Ensure the Lecture relationship is maintained if not provided in the payload.
                    // This prevents setting the required 'lecture_id' foreign key to null.
                    if (updatedPart.getLecture() == null && existingPart.getLecture() != null) {
                        updatedPart.setLecture(existingPart.getLecture());
                    }
                    
                    // In a DTO approach, you'd manually copy fields to existingPart here.
                    LecturePart savedPart = lecturePartService.savePart(updatedPart);
                    return ResponseEntity.ok(savedPart);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // ----------------------------------------------------
    // Content Management Endpoints
    // ----------------------------------------------------

    @GetMapping("/{partId}/content")
    public Object getContent(@PathVariable Integer partId) {
        return lecturePartService.getContent(partId);
    }

    @PutMapping("/{partId}/content-url") 
    public ResponseEntity<Void> updateContentUrl(@PathVariable Integer partId, @RequestBody String newContentUrl) {
        lecturePartService.updateContent(partId, newContentUrl);
        return ResponseEntity.noContent().build();
    }
}