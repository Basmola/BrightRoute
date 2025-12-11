package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.repository.LecturePartRepository;
import com.brightroute.brightroute.repository.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final LecturePartRepository lecturePartRepository;

    public LectureService(LectureRepository lectureRepository,
                          LecturePartRepository lecturePartRepository) {
        this.lectureRepository = lectureRepository;
        this.lecturePartRepository = lecturePartRepository;
    }

    // ===== Lecture CRUD =====
    public Lecture saveLecture(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    public Lecture getLectureById(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));
    }

    public void deleteLecture(Long id) {
        lectureRepository.deleteById(id);
    }

    // ===== LecturePart operations =====
    public Lecture addPart(Long lectureId, LecturePart part) {
        Lecture lecture = getLectureById(lectureId);
        part.setLecture(lecture);
        lecture.getParts().add(part);
        lecturePartRepository.save(part);
        return lectureRepository.save(lecture);
    }

    public Lecture deletePart(Long lectureId, Long partId) {
        Lecture lecture = getLectureById(lectureId);
        LecturePart part = lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found"));
        lecture.getParts().remove(part);
        lecturePartRepository.delete(part);
        return lectureRepository.save(lecture);
    }

    public Lecture updatePart(Long lectureId, LecturePart updatedPart) {
        Lecture lecture = getLectureById(lectureId);
        updatedPart.setLecture(lecture);
        lecturePartRepository.save(updatedPart);
        return lectureRepository.save(lecture);
    }

    
}

