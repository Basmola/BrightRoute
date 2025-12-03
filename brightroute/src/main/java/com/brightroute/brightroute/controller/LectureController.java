package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.service.LectureService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    // ===== Lecture endpoints =====
    @PostMapping
    public Lecture createLecture(@RequestBody Lecture lecture) {
        return lectureService.saveLecture(lecture);
    }

    @GetMapping
    public List<Lecture> getAllLectures() {
        return lectureService.getAllLectures();
    }

    @GetMapping("/{id}")
    public Lecture getLecture(@PathVariable Long id) {
        return lectureService.getLectureById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteLecture(@PathVariable Long id) {
        lectureService.deleteLecture(id);
    }

    // ===== LecturePart endpoints =====
    @PostMapping("/{lectureId}/parts")
    public Lecture addPart(@PathVariable Long lectureId, @RequestBody LecturePart part) {
        return lectureService.addPart(lectureId, part);
    }

    @DeleteMapping("/{lectureId}/parts/{partId}")
    public Lecture deletePart(@PathVariable Long lectureId, @PathVariable Long partId) {
        return lectureService.deletePart(lectureId, partId);
    }

    @PutMapping("/{lectureId}/parts")
    public Lecture updatePart(@PathVariable Long lectureId, @RequestBody LecturePart part) {
        return lectureService.updatePart(lectureId, part);
    }

    @GetMapping("/{lectureId}/average-rating")
    public Double getAverageRating(@PathVariable Long lectureId) {
        return lectureService.getAverageRating(lectureId);
    }
}
