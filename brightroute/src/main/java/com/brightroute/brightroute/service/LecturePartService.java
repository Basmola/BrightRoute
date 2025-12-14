package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.repository.LecturePartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LecturePartService {

    private final LecturePartRepository lecturePartRepository;

    public LecturePartService(LecturePartRepository lecturePartRepository) {
        this.lecturePartRepository = lecturePartRepository;
    }

    public List<LecturePart> getAllParts() {
        return lecturePartRepository.findAll();
    }

    public Optional<LecturePart> findPartById(Integer partId) {
        return lecturePartRepository.findById(partId);
    }

    @Transactional
    public LecturePart savePart(LecturePart part) {
        return lecturePartRepository.save(part);
    }

    private LecturePart getPartByIdOrThrow(Integer partId) {
        return lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found for ID: " + partId));
    }

    public Object getContent(Integer partId) {
        LecturePart part = getPartByIdOrThrow(partId);

        return part.getPartContentUrl();
    }

    @Transactional
    public void updateContent(Integer partId, String newContentUrl) {
        LecturePart part = getPartByIdOrThrow(partId);

        if ("QUIZ".equals(part.getPartType())) {
            throw new IllegalArgumentException(
                    "Cannot update content URL for a QUIZ part. Update Quiz entity instead.");
        }

        part.setPartContentUrl(newContentUrl);
        lecturePartRepository.save(part);

    }

    @Transactional
    public boolean deletePartById(Integer partId) {
        if (lecturePartRepository.existsById(partId)) {
            lecturePartRepository.deleteById(partId);
            return true;
        }
        return false;
    }
}