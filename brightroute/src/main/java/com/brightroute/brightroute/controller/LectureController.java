package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.service.LectureService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/lectures") // Renamed to include /api prefix for consistency
public class LectureController {

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @PostMapping
    public Lecture createLecture(@RequestBody Lecture lecture) {
        return lectureService.saveLecture(lecture);
    }

    @GetMapping
    public List<Lecture> getAllLectures() {
        return lectureService.getAllLectures();
    }

    @GetMapping("/{id}")
    public Lecture getLecture(@PathVariable Integer id) { // CORRECTION: Integer
        return lectureService.getLectureById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteLecture(@PathVariable Integer id) { // CORRECTION: Integer
        lectureService.deleteLecture(id);
    }

    // ===== LecturePart endpoints =====
    @PostMapping("/{lectureId}/parts")
    public Lecture addPart(@PathVariable Integer lectureId, @RequestBody LecturePart part) { // CORRECTION: Integer
        return lectureService.addPart(lectureId, part);
    }

    @DeleteMapping("/{lectureId}/parts/{partId}")
    public Lecture deletePart(@PathVariable Integer lectureId, @PathVariable Integer partId) { // CORRECTION: Integer
        return lectureService.deletePart(lectureId, partId);
    }

    @PutMapping("/{lectureId}/parts")
    public Lecture updatePart(@PathVariable Integer lectureId, @RequestBody LecturePart part) { // CORRECTION: Integer
        return lectureService.updatePart(lectureId, part);
    }
}