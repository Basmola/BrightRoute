package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.User; // Required for the final redeem process
import com.brightroute.brightroute.repository.AccessCodeRepository;
import com.brightroute.brightroute.repository.CourseRepository; // ADDED
import com.brightroute.brightroute.repository.LectureRepository;
import com.brightroute.brightroute.repository.UserRepository; // Placeholder for User
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccessCodeService {

    @Autowired
    private AccessCodeRepository accessCodeRepository;

    @Autowired
    private CourseRepository courseRepository; // Necessary for NOT NULL FK

    @Autowired
    private LectureRepository lectureRepository;
    
    // NOTE: Uncomment and inject the UserRepository when it's fully defined
    // @Autowired 
    // private UserRepository userRepository; 

    // إنشاء كود جديد مربوط بكورس + محاضرة + يوزر
    public AccessCode createAccessCode(Integer courseId, Integer lectureId, Integer userId, String codeValue) { 
        // CORRECT: All IDs are now Integer

        // 1. Fetch Course (required for NOT NULL FK)
        Course course = courseRepository.findById(courseId)
                     .orElseThrow(() -> new RuntimeException("Course not found for ID: " + courseId));

        // 2. Fetch Lecture (optional)
        Lecture lecture = null;
        if (lectureId != null) {
            lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new RuntimeException("Lecture not found for ID: " + lectureId));
        }

        // 3. Fetch User (optional, used to pre-link the code creator/intended recipient)
        // User user = null;
        // if (userId != null) {
        //     user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // }
        
        AccessCode code = new AccessCode();
        code.setCourse(course); 
        code.setLecture(lecture);
        // code.setUsedBy(user); // User can be the code creator or intended recipient at creation
        
        code.setCodeIsUsed(false); 
        code.setCodeCreatedAt(LocalDateTime.now());
        
        // 4. Set code value
        code.setCodeValue(codeValue != null ? codeValue : UUID.randomUUID().toString());

        return accessCodeRepository.save(code);
    }

    // التحقق من صلاحية الكود
    public boolean validateAccessCode(String codeValue) {
        return accessCodeRepository.findByCodeValue(codeValue)
                // CORRECT: Use getCodeIsUsed() and check expiry
                .filter(c -> !c.getCodeIsUsed()) 
                .filter(c -> c.getCodeExpiresAt() == null || c.getCodeExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    // إلغاء/حذف الكود
    public void revokeAccessCode(Integer id) { // CORRECT: ID is Integer
        AccessCode code = accessCodeRepository.findById(id) // CORRECT: findById takes Integer
                .orElseThrow(() -> new RuntimeException("AccessCode not found for ID: " + id));
        
        // CORRECT: Use setCodeIsUsed()
        code.setCodeIsUsed(true); 
        code.setCodeUsedAt(LocalDateTime.now());
        accessCodeRepository.save(code);
    }
}