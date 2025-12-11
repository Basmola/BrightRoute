package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.Student;
import com.brightroute.brightroute.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<Student> create(
            @PathVariable Long userId,
            @RequestBody Student profile
    ) {
        return ResponseEntity.ok(studentService.createStudentProfile(userId, profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> get(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.viewStudent(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(
            @PathVariable Long id,
            @RequestBody Student updated
    ) {
        return ResponseEntity.ok(studentService.updateStudent(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
