package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.LecturePart; // Assuming you'll create a DTO or use the model for content updates
import com.brightroute.brightroute.service.LecturePartService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/lecture-parts") // Consistency: Added /api prefix
public class LecturePartController {

    private final LecturePartService lecturePartService;

    @Autowired // Add Autowired for clarity (though constructor injection is implicit)
    public LecturePartController(LecturePartService lecturePartService) {
        this.lecturePartService = lecturePartService;
    }

    // ===== Get content =====
    @GetMapping("/{partId}/content")
    public Object getContent(@PathVariable Integer partId) { // CORRECTION: Integer
        return lecturePartService.getContent(partId);
    }

    // ===== Update content =====
    @PutMapping("/{partId}/content")
    public void updateContent(@PathVariable Integer partId, @RequestBody String newContentUrl) { // CORRECTION: Integer
        lecturePartService.updateContent(partId, newContentUrl);
    }
    
    // NOTE: You would typically add GET/PUT/DELETE for the LecturePart itself here.
}