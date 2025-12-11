package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.Lecture;
import com.brightroute.brightroute.model.LecturePart;
import com.brightroute.brightroute.model.Student;
import com.brightroute.brightroute.model.SystemLog;
import com.brightroute.brightroute.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/promote/{userId}")
    public ResponseEntity<User> promote(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.promote(userId));
    }

    @PostMapping("/demote/{userId}")
    public ResponseEntity<User> demote(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.demote(userId));
    }

    @PostMapping("/suspend/{userId}")
    public ResponseEntity<Void> suspend(@PathVariable Long userId) {
        adminService.suspend(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/activate/{userId}")
    public ResponseEntity<Void> activate(@PathVariable Long userId) {
        adminService.activate(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(adminService.allUsers());
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> allStudents() {
        return ResponseEntity.ok(adminService.allStudents());
    }
    @PostMapping("/courses")
public ResponseEntity<Course> addCourse(@RequestBody Course course) {
    return ResponseEntity.ok(adminService.addCourse(course));
}

@PutMapping("/courses/{id}")
public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updated) {
    return ResponseEntity.ok(adminService.updateCourse(id, updated));
}

@DeleteMapping("/courses/{id}")
public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
    adminService.deleteCourse(id);
    return ResponseEntity.noContent().build();
}

// lectures
@PostMapping("/lectures")
public ResponseEntity<Lecture> addLecture(@RequestBody Lecture lecture) {
    return ResponseEntity.ok(adminService.addLecture(lecture));
}

// lecture parts
@PostMapping("/lecture-parts")
public ResponseEntity<LecturePart> addPart(@RequestBody LecturePart part) {
    return ResponseEntity.ok(adminService.addPart(part));
}

// quizzes
@PostMapping("/quizzes")
public ResponseEntity<Quiz> addQuiz(@RequestBody Quiz quiz) {
    return ResponseEntity.ok(adminService.addQuiz(quiz));
}

// access codes
@PostMapping("/codes/course/{courseId}")
public ResponseEntity<AccessCode> generateCode(@PathVariable Long courseId, @RequestParam(required=false) Integer validMinutes) {
    return ResponseEntity.ok(adminService.generateCodeForCourse(courseId, validMinutes));
}

// enroll
@PostMapping("/enroll/{userId}/{lectureId}")
public ResponseEntity<Enrollment> enroll(@PathVariable Long userId, @PathVariable Long lectureId) {
    return ResponseEntity.ok(adminService.enrollUser(userId, lectureId));
}

// analytics
@GetMapping("/analytics")
public ResponseEntity<Map<String,Object>> analytics() {
    return ResponseEntity.ok(adminService.viewAnalytics());
}

// logs
@GetMapping("/logs")
public ResponseEntity<List<SystemLog>> logs() {
    return ResponseEntity.ok(adminService.viewSystemLogs());
}
}
