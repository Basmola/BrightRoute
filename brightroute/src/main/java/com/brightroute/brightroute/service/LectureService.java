package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.repository.LecturePartRepository;
import com.brightroute.brightroute.repository.LectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for best practice

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

    public Lecture getLectureById(Integer id) { // CORRECTION: Integer
        return lectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecture not found for ID: " + id));
    }

    public void deleteLecture(Integer id) { // CORRECTION: Integer
        lectureRepository.deleteById(id);
    }

    // ===== LecturePart operations =====
    @Transactional // Ensures the collection modification and save are atomic
    public Lecture addPart(Integer lectureId, LecturePart part) { // CORRECTION: Integer
        Lecture lecture = getLectureById(lectureId);
        
        // 1. Set the bidirectional relationship
        part.setLecture(lecture);
        
        // 2. Add to the collection (important for JPA context)
        lecture.getParts().add(part);
        
        // 3. Save the owning side (Lecture)
        // lecturePartRepository.save(part); // This save is redundant due to CascadeType.ALL
        return lectureRepository.save(lecture); 
    }

    @Transactional
    public Lecture deletePart(Integer lectureId, Integer partId) { // CORRECTION: Integer
        Lecture lecture = getLectureById(lectureId);
        
        // Find the part (use findById for cleaner error handling)
        LecturePart partToDelete = lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found for ID: " + partId));
        
        // 1. Check if the part belongs to this lecture (Good Business Logic)
        if (!partToDelete.getLecture().getId().equals(lectureId)) {
            throw new RuntimeException("Part ID " + partId + " does not belong to Lecture ID " + lectureId);
        }
        
        // 2. Remove from the collection (triggers orphanRemoval and delete)
        lecture.getParts().remove(partToDelete);
        
        // 3. Save the owning side
        return lectureRepository.save(lecture);
    }

    @Transactional
    public Lecture updatePart(Integer lectureId, LecturePart updatedPart) { // CORRECTION: Integer
        // Fetch lecture to ensure part belongs here
        Lecture lecture = getLectureById(lectureId);
        
        // The ID of the part must exist in the updatedPart object
        if (updatedPart.getId() == null) {
            throw new IllegalArgumentException("Updated part must contain an ID.");
        }
        
        // 1. Ensure the updated part references the current lecture
        updatedPart.setLecture(lecture);
        
        // 2. Save the part (JPA will merge/update based on ID)
        lecturePartRepository.save(updatedPart);
        
        // 3. Re-fetch or return the lecture to ensure the response reflects the update
        return getLectureById(lectureId); 
    }
}