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
    // Core CRUD Operations (for the LecturePart entity itself)
    // ----------------------------------------------------

    /**
     * Retrieves all LectureParts (useful for debugging/admin views).
     */
    @GetMapping
    public List<LecturePart> getAllParts() {
        return lecturePartService.getAllParts();
    }
    
    /**
     * Retrieves a single LecturePart by ID.
     */
    @GetMapping("/{partId}")
    public ResponseEntity<LecturePart> getPartById(@PathVariable Integer partId) {
        return lecturePartService.findPartById(partId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing LecturePart (excluding its content URL).
     * NOTE: Creation and Deletion are often handled via the LectureController.
     */
    @PutMapping("/{partId}")
    public ResponseEntity<LecturePart> updatePart(
            @PathVariable Integer partId,
            @RequestBody LecturePart updatedPart) {
        
        return lecturePartService.findPartById(partId)
                .map(existingPart -> {
                    updatedPart.setId(partId); // Ensure the ID from the path is used
                    
                    // You would typically copy fields from updatedPart to existingPart here
                    // to prevent overwriting managed fields (like Lecture/Quiz relations).
                    
                    // For simplicity, we delegate the update logic to the service's save/merge.
                    LecturePart savedPart = lecturePartService.savePart(updatedPart);
                    return ResponseEntity.ok(savedPart);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // ----------------------------------------------------
    // Content Management Endpoints (as defined by you)
    // ----------------------------------------------------

    /**
     * Retrieves the content (URL, Quiz object, etc.) for a specific lecture part.
     */
    @GetMapping("/{partId}/content")
    public Object getContent(@PathVariable Integer partId) {
        return lecturePartService.getContent(partId);
    }

    /**
     * Updates the content URL for a specific lecture part (e.g., changing a video link).
     */
    @PutMapping("/{partId}/content-url") // Renamed endpoint for clarity
    public ResponseEntity<Void> updateContentUrl(@PathVariable Integer partId, @RequestBody String newContentUrl) {
        lecturePartService.updateContent(partId, newContentUrl);
        return ResponseEntity.noContent().build();
    }
}