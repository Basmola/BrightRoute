package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.AccessCodeRepository;
import com.brightroute.brightroute.repository.CourseRepository;
import com.brightroute.brightroute.repository.LectureRepository;
import com.brightroute.brightroute.repository.UserRepository;
// استيراد الاستثناءات المخصصة الجديدة
import com.brightroute.brightroute.Exceptions.AccessCodeUsedException;
import com.brightroute.brightroute.Exceptions.AccessCodeNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // تم إضافة هذا للاستخدام لاحقًا إن لزم الأمر.

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// 💡 ملاحظة: يجب عليك إنشاء فئتي AccessCodeUsedException و AccessCodeNotFoundException
// في الحزمة com.brightroute.brightroute.exception لوقف خطأ 500 وتحويله إلى 409/404.

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

    // إنشاء كود جديد مربوط بكورس + محاضرة + يوزر
    public AccessCode createAccessCode(Integer courseId, Integer lectureId, Integer userId, String codeValue) {
        // 1. Fetch Course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AccessCodeNotFoundException("Course not found for ID: " + courseId)); // استخدام
                                                                                                             // استثناء
                                                                                                             // مخصص

        // 2. Fetch Lecture (optional)
        Lecture lecture = null;
        if (lectureId != null) {
            lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new AccessCodeNotFoundException("Lecture not found for ID: " + lectureId)); // استخدام
                                                                                                                   // استثناء
                                                                                                                   // مخصص
        }

        // 3. Fetch User (optional, intended recipient/creator)
        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new AccessCodeNotFoundException("User not found for ID: " + userId)); // استخدام
                                                                                                             // استثناء
                                                                                                             // مخصص
        }

        AccessCode code = new AccessCode();
        code.setCourse(course);
        code.setLecture(lecture);
        code.setUsedBy(user);

        code.setCodeIsUsed(false);
        code.setCodeCreatedAt(LocalDateTime.now());

        // 4. Set code value (random if null)
        code.setCodeValue(codeValue != null ? codeValue : UUID.randomUUID().toString());

        return accessCodeRepository.save(code);
    }

    // التحقق من صلاحية الكود
    public boolean validateAccessCode(String codeValue) {
        return accessCodeRepository.findByCodeValue(codeValue)
                .filter(c -> !c.getCodeIsUsed())
                .filter(c -> c.getCodeExpiresAt() == null || c.getCodeExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    // الدالة المحدثة: استخدام/استرداد الكود (Redeem)
    // 💡 تم إضافة @Transactional لجعل العملية ككل Transaction واحدة (عادةً ما يساعد
    // في JPA)
    @Transactional
    public AccessCode redeemAccessCode(String codeValue, Integer userId, Integer lectureId) {

        // 1. البحث عن الكود والتحقق الأولي من الصلاحية
        AccessCode code = accessCodeRepository.findByCodeValue(codeValue)
                .orElseThrow(() -> new AccessCodeNotFoundException("Access Code not found.")); // 💡 الآن سيرجع 404

        if (code.getCodeIsUsed()) {
            throw new AccessCodeUsedException("Access Code is already used."); // 💡 الآن سيرجع 409
        }
        if (code.getCodeExpiresAt() != null && code.getCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AccessCodeUsedException("Access Code is expired."); // 💡 الآن سيرجع 409
        }

        // 2. جلب المستخدم (إلزامي لعملية الاسترداد)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AccessCodeNotFoundException("User not found for ID: " + userId));

        // 3. جلب المحاضرة (اختياري، يتم تعيينه عند الاستخدام)
        Lecture lecture = null;
        if (lectureId != null) {
            lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new AccessCodeNotFoundException("Lecture not found for ID: " + lectureId));
        }

        // 4. تطبيق التغييرات (إلغاء الصلاحية بعد الاستخدام)
        code.setCodeIsUsed(true);
        code.setCodeUsedAt(LocalDateTime.now());

        // 5. ربط المستخدم والمحاضرة (UsedBy و Lecture)
        code.setUsedBy(user);
        if (lecture != null) {
            code.setLecture(lecture);
        }

        AccessCode redeemedCode = accessCodeRepository.save(code);

        // 🚀 حل مشكلة 500/LazyInitializationException:
        if (redeemedCode.getCourse() != null) {
            redeemedCode.getCourse().getCourseTitle(); // Force initialization of Course proxy
        }

        return redeemedCode;
    }

    // إلغاء/حذف الكود (Revoke)
    @Transactional
    public void revokeAccessCode(Integer id) {
        AccessCode code = accessCodeRepository.findById(id)
                .orElseThrow(() -> new AccessCodeNotFoundException("AccessCode not found for ID: " + id));

        code.setCodeIsUsed(true);
        code.setCodeUsedAt(LocalDateTime.now());
        accessCodeRepository.save(code);
    }

    // NEW: Get all access codes
    public List<AccessCode> getAllAccessCodes() {
        return accessCodeRepository.findAll();
    }
}