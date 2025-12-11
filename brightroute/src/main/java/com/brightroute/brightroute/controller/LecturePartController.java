package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.service.LecturePartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lecture-parts")
public class LecturePartController {

    private final LecturePartService lecturePartService;

    public LecturePartController(LecturePartService lecturePartService) {
        this.lecturePartService = lecturePartService;
    }

    // ===== Get content =====
    @GetMapping("/{partId}/content")
    public Object getContent(@PathVariable Long partId) {
        return lecturePartService.getContent(partId);
    }

    // ===== Update content =====
    @PutMapping("/{partId}/content")
    public void updateContent(@PathVariable Long partId, @RequestBody String newContentUrl) {
        lecturePartService.updateContent(partId, newContentUrl);
    }
}

