package com.brightroute.brightroute.service;

import com.brightroute.brightroute.enums.Role;
import com.brightroute.brightroute.model.Student;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.StudentRepository;
import com.brightroute.brightroute.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for best practice

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired 
    private UserRepository userRepository;

    // CREATE STUDENT PROFILE
    @Transactional // Ensures both user update and profile save are atomic
    public Student createStudentProfile(Integer userId, Student profile) { // ID type is correct (Integer)

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Assign 1-1 relationship and shared primary key
        profile.setUser(user);
        profile.setId(userId);

        // Enforce role consistency (change base user role to STUDENT)
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        return studentRepository.save(profile);
    }

    // VIEW STUDENT PROFILE
    public Student viewStudent(Integer id) { // ID type is correct (Integer)
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));
    }

    // UPDATE
    @Transactional
    public Student updateStudent(Integer id, Student updated) { // ID type is correct (Integer)

        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));

        if (updated.getNationalId() != null) s.setNationalId(updated.getNationalId());
        if (updated.getParentNumber() != null) s.setParentNumber(updated.getParentNumber());
        if (updated.getIdType() != null) s.setIdType(updated.getIdType());
        if (updated.getLevelOfEducation() != null) s.setLevelOfEducation(updated.getLevelOfEducation());
        if (updated.getNationalIdFront() != null) s.setNationalIdFront(updated.getNationalIdFront());
        if (updated.getBirthCertificate() != null) s.setBirthCertificate(updated.getBirthCertificate());

        return studentRepository.save(s);
    }

    // DELETE PROFILE
    public void deleteStudent(Integer id) { // ID type is correct (Integer)
        // Deleting the Student entity will cascade via ON DELETE CASCADE in SQL,
        // but typically you delete the base User to cascade deletion of all profile data.
        // For simplicity, we stick to deleting the profile here.
        studentRepository.deleteById(id);
    }
}