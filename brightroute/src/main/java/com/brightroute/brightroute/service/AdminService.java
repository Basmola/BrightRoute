
package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.*;
import com.brightroute.brightroute.repository.*;

import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.model.Student;
import com.brightroute.brightroute.repository.UserRepository;
import com.brightroute.brightroute.repository.StudentRepository;


import org.springframework.stereotype.Service;

import java.util.List;
import com.brightroute.brightroute.service.ICourseOperation;
import com.brightroute.brightroute.service.ILectureOperation;
import com.brightroute.brightroute.service.ILecturePartOperation;
import com.brightroute.brightroute.service.IQuizOperation;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
@Transactional
public class AdminService implements ICourseOperation, ILectureOperation, ILecturePartOperation, IQuizOperation {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private LecturePartRepository lecturePartRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionsChoiceRepository questionsChoiceRepository;

    @Autowired
    private AccessCodeRepository accessCodeRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseSubscriptionRepository courseSubscriptionRepository;

    @Autowired
    private LectureAccessRepository lectureAccessRepository;

    @Autowired
    private StudentQuizSubmissionRepository studentQuizSubmissionRepository;

    @Autowired
    private StudentQuestionAnswerRepository studentQuestionAnswerRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    // --- User management (diagram methods)
    public User promoteToAdmin(Long userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        u.setRole("ADMIN");
        return userRepository.save(u);
    }

    public User demoteAdmin(Long userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        u.setRole("STUDENT");
        return userRepository.save(u);
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
    }

    public void suspendUser(Long userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        u.setAccountStatus("SUSPENDED");
        userRepository.save(u);
        logAction(u, "SUSPEND_USER", "Suspended user with id " + userId);
    }

    public void activateUser(Long userId) {
        User u = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        u.setAccountStatus("ACTIVE");
        userRepository.save(u);
        logAction(u, "ACTIVATE_USER", "Activated user with id " + userId);
    }

    // student management
    public List<Student> listAllStudents() {
        return studentRepository.findAll();
    }

    public Student viewStudent(Long studentId) {
        return studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found."));
    }

    // ---- ICourseOperation implementations
    @Override
    public Course addCourse(Course course) {
        course.setCreatedAt(LocalDateTime.now());
        Course saved = courseRepository.save(course);
        logAction(null, "ADD_COURSE", "Added course id=" + saved.getId());
        return saved;
    }

    @Override
    public Course updateCourse(Long courseId, Course updated) {
        return courseRepository.findById(courseId).map(c -> {
            if (updated.getTitle() != null) c.setTitle(updated.getTitle());
            if (updated.getDescription() != null) c.setDescription(updated.getDescription());
            if (updated.getImageCover() != null) c.setImageCover(updated.getImageCover());
            if (updated.getInstructor() != null) c.setInstructor(updated.getInstructor());
            if (updated.getNumberOfLectures() != null) c.setNumberOfLectures(updated.getNumberOfLectures());
            Course saved = courseRepository.save(c);
            logAction(null, "UPDATE_COURSE", "Updated course id=" + courseId);
            return saved;
        }).orElseThrow(() -> new RuntimeException("Course not found."));
    }

    @Override
    public void deleteCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) throw new RuntimeException("Course not found.");
        // cascade deletions are handled by JPA relations if configured
        courseRepository.deleteById(courseId);
        logAction(null, "DELETE_COURSE", "Deleted course id=" + courseId);
    }

    // ---- ILectureOperation implementations
    @Override
    public Lecture addLecture(Lecture lecture) {
        lecture.setCreatedAt(LocalDateTime.now());
        Lecture saved = lectureRepository.save(lecture);

        // update course number of lectures if course present
        if (lecture.getCourse() != null && lecture.getCourse().getId() != null) {
            courseRepository.findById(lecture.getCourse().getId()).ifPresent(c -> {
                Integer curr = c.getNumberOfLectures() == null ? 0 : c.getNumberOfLectures();
                c.setNumberOfLectures(curr + 1);
                courseRepository.save(c);
            });
        }

        logAction(null, "ADD_LECTURE", "Added lecture id=" + saved.getId());
        return saved;
    }

    @Override
    public Lecture updateLecture(Long lectureId, Lecture updated) {
        return lectureRepository.findById(lectureId).map(l -> {
            if (updated.getTitle() != null) l.setTitle(updated.getTitle());
            if (updated.getDescription() != null) l.setDescription(updated.getDescription());
            if (updated.getImage() != null) l.setImage(updated.getImage());
            if (updated.getOrderNumber() != null) l.setOrderNumber(updated.getOrderNumber());
            Lecture saved = lectureRepository.save(l);
            logAction(null, "UPDATE_LECTURE", "Updated lecture id=" + lectureId);
            return saved;
        }).orElseThrow(() -> new RuntimeException("Lecture not found."));
    }

    @Override
    public void deleteLecture(Long lectureId) {
        if (!lectureRepository.existsById(lectureId)) throw new RuntimeException("Lecture not found.");
        lectureRepository.deleteById(lectureId);
        logAction(null, "DELETE_LECTURE", "Deleted lecture id=" + lectureId);
    }

    // ---- ILecturePartOperation implementations
    @Override
    public LecturePart addPart(LecturePart part) {
        LecturePart saved = lecturePartRepository.save(part);
        logAction(null, "ADD_PART", "Added lecture part id=" + saved.getId());
        return saved;
    }

    @Override
    public LecturePart updatePart(Long partId, LecturePart updated) {
        return lecturePartRepository.findById(partId).map(p -> {
            if (updated.getPartType() != null) p.setPartType(updated.getPartType());
            if (updated.getContentUrl() != null) p.setContentUrl(updated.getContentUrl());
            if (updated.getDescription() != null) p.setDescription(updated.getDescription());
            if (updated.getOrderNumber() != null) p.setOrderNumber(updated.getOrderNumber());
            LecturePart saved = lecturePartRepository.save(p);
            logAction(null, "UPDATE_PART", "Updated lecture part id=" + partId);
            return saved;
        }).orElseThrow(() -> new RuntimeException("Lecture part not found."));
    }

    @Override
    public void deletePart(Long partId) {
        if (!lecturePartRepository.existsById(partId)) throw new RuntimeException("Lecture part not found.");
        lecturePartRepository.deleteById(partId);
        logAction(null, "DELETE_PART", "Deleted lecture part id=" + partId);
    }

    // ---- IQuizOperation implementations
    @Override
    public Quiz addQuiz(Quiz quiz) {
        Quiz saved = quizRepository.save(quiz);
        logAction(null, "ADD_QUIZ", "Added quiz id=" + saved.getId());
        return saved;
    }

    @Override
    public Quiz updateQuiz(Long quizId, Quiz updated) {
        return quizRepository.findById(quizId).map(q -> {
            if (updated.getQuestionText() != null) q.setQuestionText(updated.getQuestionText());
            if (updated.getQuestionImage() != null) q.setQuestionImage(updated.getQuestionImage());
            Quiz saved = quizRepository.save(q);
            logAction(null, "UPDATE_QUIZ", "Updated quiz id=" + quizId);
            return saved;
        }).orElseThrow(() -> new RuntimeException("Quiz not found."));
    }

    @Override
    public void deleteQuiz(Long quizId) {
        if (!quizRepository.existsById(quizId)) throw new RuntimeException("Quiz not found.");
        quizRepository.deleteById(quizId);
        logAction(null, "DELETE_QUIZ", "Deleted quiz id=" + quizId);
    }

    // ---- Other admin methods from class diagram (implemented)

    /**
     * Manage access codes: cleanup expired codes and optionally mark stale unused ones.
     * This is a reasonable "manage" implementation that performs maintenance.
     */
    public void manageAccessCodes() {
        List<AccessCode> all = accessCodeRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (AccessCode ac : all) {
            if (ac.getExpiresAt() != null && ac.getExpiresAt().isBefore(now) && !Boolean.TRUE.equals(ac.getIsUsed())) {
                ac.setIsUsed(true); // mark expired codes as used to prevent redemption
                accessCodeRepository.save(ac);
                logAction(null, "CLEANUP_EXPIRED_CODE", "Expired code marked used: " + ac.getCodeValue());
            }
        }
    }

    /**
     * Manage enrollments: example implementation — completes enrollments older than 365 days.
     */
    public void manageEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (Enrollment e : enrollments) {
            if (e.getDateEnrolled() != null && e.getDateEnrolled().plusDays(365).isBefore(now) && !"COMPLETED".equals(e.getStatus())) {
                e.setStatus("COMPLETED");
                enrollmentRepository.save(e);
                logAction(e.getUser(), "COMPLETE_ENROLLMENT", "Auto-completed enrollment id=" + e.getId());
            }
        }
    }

    /**
     * Manage subscriptions: example implementation — remove duplicate subscriptions for same user-course.
     */
    public void manageSubscriptions() {
        List<CourseSubscription> subs = courseSubscriptionRepository.findAll();
        Set<String> seen = new HashSet<>();
        for (CourseSubscription s : subs) {
            String key = s.getUser().getId() + "-" + s.getCourse().getId();
            if (seen.contains(key)) {
                courseSubscriptionRepository.deleteById(s.getId());
                logAction(s.getUser(), "REMOVE_DUP_SUBSCRIPTION", "Removed duplicate subscription id=" + s.getId());
            } else {
                seen.add(key);
            }
        }
    }

    /**
     * Update the choice text for a given QuestionsChoice.
     */
    public void setChoiceText(Long choiceId, String text) {
        QuestionsChoice ch = questionsChoiceRepository.findById(choiceId)
                .orElseThrow(() -> new RuntimeException("Choice not found."));
        ch.setText(text);
        questionsChoiceRepository.save(ch);
        logAction(null, "SET_CHOICE_TEXT", "Updated choice id=" + choiceId);
    }

    /**
     * Update the explanation for a given QuestionsChoice.
     */
    public void setExplanation(Long choiceId, String explanation) {
        QuestionsChoice ch = questionsChoiceRepository.findById(choiceId)
                .orElseThrow(() -> new RuntimeException("Choice not found."));
        ch.setExplanationText(explanation);
        questionsChoiceRepository.save(ch);
        logAction(null, "SET_CHOICE_EXPLANATION", "Updated explanation for choice id=" + choiceId);
    }

    /**
     * Basic analytics implementation that returns a few useful counts.
     * Returns Object per your previous signature (Map<String,Object>).
     */
    public Object viewAnalytics() {
        Map<String, Object> map = new HashMap<>();
        map.put("totalUsers", userRepository.count());
        map.put("totalStudents", studentRepository.count());
        map.put("totalCourses", courseRepository.count());
        map.put("totalLectures", lectureRepository.count());
        map.put("totalEnrollments", enrollmentRepository.count());
        map.put("totalSubscriptions", courseSubscriptionRepository.count());
        return map;
    }
=
    public List<SystemLog> viewSystemLogs() {
        return systemLogRepository.findAll();
    }

=
    public String generateCode() {
        String code = "CODE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        AccessCode ac = new AccessCode();
        ac.setCodeValue(code);
        ac.setIsUsed(false);
        ac.setCreatedAt(LocalDateTime.now());
        accessCodeRepository.save(ac);
        logAction(null, "GENERATE_CODE", "Generated code " + code);
        return code;
    }


    private void logAction(User user, String action, String details) {
        SystemLog log = new SystemLog();
        log.setUser(user);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        systemLogRepository.save(log);
    }
}
