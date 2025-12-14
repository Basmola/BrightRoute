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
                    updatedPart.setId(partId);  

                    if (updatedPart.getLecture() == null && existingPart.getLecture() != null) {
                        updatedPart.setLecture(existingPart.getLecture());
                    }

                    LecturePart savedPart = lecturePartService.savePart(updatedPart);
                    return ResponseEntity.ok(savedPart);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{partId}/content")
    public Object getContent(@PathVariable Integer partId) {
        return lecturePartService.getContent(partId);
    }

    @PutMapping("/{partId}/content-url") 
    public ResponseEntity<Void> updateContentUrl(@PathVariable Integer partId, @RequestBody String newContentUrl) {
        lecturePartService.updateContent(partId, newContentUrl);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{partId}")
    public ResponseEntity<Void> deletePart(@PathVariable Integer partId) {
        boolean isDeleted = lecturePartService.deletePartById(partId);

        if (isDeleted) {
             
            return ResponseEntity.noContent().build();
        } else {
             
            return ResponseEntity.notFound().build();
        }
    }
}