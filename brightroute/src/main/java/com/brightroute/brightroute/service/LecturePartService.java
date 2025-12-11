package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.repository.LecturePartRepository;
import org.springframework.stereotype.Service;

@Service
public class LecturePartService {

    private final LecturePartRepository lecturePartRepository;

    public LecturePartService(LecturePartRepository lecturePartRepository) {
        this.lecturePartRepository = lecturePartRepository;
    }

    // ===== Get content =====
    public Object getContent(Integer partId) {
        LecturePart part = lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found"));
        return part.getPartContentUrl(); // رجعنا URL بس لأنه ده اللي موجود
    }

    // ===== Update content =====
    public void updateContent(Integer partId, String newContentUrl) {
        LecturePart part = lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found"));
        part.setPartContentUrl(newContentUrl);
        lecturePartRepository.save(part);
    }
}


