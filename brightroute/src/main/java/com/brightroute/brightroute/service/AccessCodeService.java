package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.Lecture;
// import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.AccessCodeRepository;
// import com.brightroute.brightroute.repository.CourseRepository;
import com.brightroute.brightroute.repository.LectureRepository;
// import com.brightroute.brightroute.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccessCodeService {

    @Autowired
    private AccessCodeRepository accessCodeRepository;

    // مش هنستخدمه دلوقتي
    // @Autowired
    // private CourseRepository courseRepository;

    @Autowired
    private LectureRepository lectureRepository;

    // @Autowired
    // private UserRepository userRepository;

    // إنشاء كود جديد مربوط بمحاضرة + يوزر (الكورس متشال دلوقتي)
    public AccessCode createAccessCode(Long courseId, Long lectureId, Long userId, String codeValue) {
        // Course course = courseRepository.findById(courseId)
        //         .orElseThrow(() -> new RuntimeException("Course not found"));

        Lecture lecture = null;
        if (lectureId != null) {
            lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new RuntimeException("Lecture not found"));
        }

        // User user = null;
        // if (userId != null) {
        //     user = userRepository.findById(userId)
        //             .orElseThrow(() -> new RuntimeException("User not found"));
        // }

        AccessCode code = new AccessCode();
        // code.setCourse(course); // متشال دلوقتي
        code.setLecture(lecture);
        // code.setUsedBy(user);
        code.setCodeValue(codeValue != null ? codeValue : UUID.randomUUID().toString());
        code.setUsed(false);
        code.setCreatedAt(LocalDateTime.now());

        return accessCodeRepository.save(code);
    }

    // التحقق من صلاحية الكود
    public boolean validateAccessCode(String codeValue) {
        return accessCodeRepository.findByCodeValue(codeValue)
                .filter(c -> !c.isUsed())
                .isPresent();
    }

    // إلغاء/حذف الكود (mark as used)
    public void revokeAccessCode(Long id) {
        AccessCode code = accessCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AccessCode not found"));
        code.setUsed(true);
        code.setUsedAt(LocalDateTime.now());
        accessCodeRepository.save(code);
    }
}
