package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.AccessCodeRepository;
import com.brightroute.brightroute.repository.CourseRepository;
import com.brightroute.brightroute.repository.LectureRepository;
import com.brightroute.brightroute.repository.UserRepository;
 
import com.brightroute.brightroute.Exceptions.AccessCodeUsedException;
import com.brightroute.brightroute.Exceptions.AccessCodeNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;  

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccessCodeService {

    @Autowired
    private AccessCodeRepository accessCodeRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IEnrollmentService enrollmentService;

    public AccessCode createAccessCode(Integer courseId, Integer lectureId, Integer userId, String codeValue) {
         
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AccessCodeNotFoundException("Course not found for ID: " + courseId));  

        Lecture lecture = null;
        if (lectureId != null) {
            lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new AccessCodeNotFoundException("Lecture not found for ID: " + lectureId));  

        }

        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new AccessCodeNotFoundException("User not found for ID: " + userId));  

        }

        AccessCode code = new AccessCode();
        code.setCourse(course);
        code.setLecture(lecture);
        code.setUsedBy(user);

        code.setCodeIsUsed(false);
        code.setCodeCreatedAt(LocalDateTime.now());

        code.setCodeValue(codeValue != null ? codeValue : UUID.randomUUID().toString());

        return accessCodeRepository.save(code);
    }

    public boolean validateAccessCode(String codeValue) {
        return accessCodeRepository.findByCodeValue(codeValue)
                .filter(c -> !c.getCodeIsUsed())
                .filter(c -> c.getCodeExpiresAt() == null || c.getCodeExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional
    public AccessCode redeemAccessCode(String codeValue, Integer userId, Integer lectureId) {

        AccessCode code = accessCodeRepository.findByCodeValue(codeValue)
                .orElseThrow(() -> new AccessCodeNotFoundException("Access Code not found."));  

        if (code.getCodeIsUsed()) {
            throw new AccessCodeUsedException("Access Code is already used.");  
        }
        if (code.getCodeExpiresAt() != null && code.getCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AccessCodeUsedException("Access Code is expired.");  
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccessCodeNotFoundException("User not found for ID: " + userId));

        Lecture lecture = null;
        if (lectureId != null) {
            lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new AccessCodeNotFoundException("Lecture not found for ID: " + lectureId));
        }

        code.setCodeIsUsed(true);
        code.setCodeUsedAt(LocalDateTime.now());

        code.setUsedBy(user);
        if (lecture != null) {
            code.setLecture(lecture);
        }

        AccessCode redeemedCode = accessCodeRepository.save(code);

        if (lectureId != null) {
            enrollmentService.enroll(lectureId, userId);
        }

        if (redeemedCode.getCourse() != null) {
            redeemedCode.getCourse().getCourseTitle();  
        }

        return redeemedCode;
    }

    @Transactional
    public void revokeAccessCode(Integer id) {
        if (!accessCodeRepository.existsById(id)) {
            throw new AccessCodeNotFoundException("AccessCode not found for ID: " + id);
        }
        accessCodeRepository.deleteById(id);
    }

    public List<AccessCode> getAllAccessCodes() {
        return accessCodeRepository.findAll();
    }
}