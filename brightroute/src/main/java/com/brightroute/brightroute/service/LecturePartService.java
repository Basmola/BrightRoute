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
    public Object getContent(Long partId) {
        LecturePart part = lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found"));
        // ممكن ترجع content byte[] أو text حسب نوعه
        if (part.getPartContent() != null) return part.getPartContent();
        return part.getPartContentText(); // fallback لو content null
    }

    // ===== Update content =====
    public LecturePart updateContent(Long partId, Object newContent) {
        LecturePart part = lecturePartRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("LecturePart not found"));

        if (newContent instanceof byte[]) {
            part.setPartContent((byte[]) newContent);
        } else if (newContent instanceof String) {
            part.setPartContentText((String) newContent);
        } else {
            throw new IllegalArgumentException("Unsupported content type");
        }

        return lecturePartRepository.save(part);
    }
}


