package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.repository.LecturePartRepository;
import com.brightroute.brightroute.repository.LectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Lecture saveLecture(Lecture lecture) {
        // CRITICAL: Ensure all child parts link back to the parent lecture
        if (lecture.getParts() != null) {
            for (LecturePart part : lecture.getParts()) {
                part.setLecture(lecture);
            }
        }
        return lectureRepository.save(lecture);
    }

    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    public Lecture getLectureById(Integer id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecture not found for ID: " + id));
    }

    public void deleteLecture(Integer id) {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        lectureRepository.deleteById(id);
    }

    // ===== LecturePart operations =====

    @Transactional
    public Lecture addPart(Integer lectureId, LecturePart part) {
        Lecture lecture = getLectureById(lectureId);
        part.setLecture(lecture);
        lecture.getParts().add(part);
        return lectureRepository.save(lecture);
    }

    @Transactional
    public Lecture deletePart(Integer lectureId, Integer partId) {
        Lecture lecture = getLectureById(lectureId);

        LecturePart partToDelete = lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found for ID: " + partId));

        if (!partToDelete.getLecture().getId().equals(lectureId)) {
            throw new RuntimeException("Part ID " + partId + " does not belong to Lecture ID " + lectureId);
        }

        lecture.getParts().remove(partToDelete);
        return lectureRepository.save(lecture);
    }

    @Transactional
    public Lecture updatePart(Integer lectureId, LecturePart updatedPart) {
        Lecture lecture = getLectureById(lectureId);

        if (updatedPart.getId() == null) {
            throw new IllegalArgumentException("Updated part must contain an ID.");
        }

        updatedPart.setLecture(lecture);
        lecturePartRepository.save(updatedPart);

        return getLectureById(lectureId);
    }
}
