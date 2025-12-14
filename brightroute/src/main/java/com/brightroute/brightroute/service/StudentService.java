package com.brightroute.brightroute.service;

import com.brightroute.brightroute.enums.Role;
import com.brightroute.brightroute.model.Student;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.StudentRepository;
import com.brightroute.brightroute.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;  

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired 
    private UserRepository userRepository;

    @Transactional  
    public Student createStudentProfile(Integer userId, Student profile) {  

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        profile.setUser(user);
        profile.setId(userId);

        user.setRole(Role.STUDENT);
        userRepository.save(user);

        return studentRepository.save(profile);
    }

    public Student viewStudent(Integer id) {  
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found."));
    }

    @Transactional
    public Student updateStudent(Integer id, Student updated) {  

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

    public void deleteStudent(Integer id) {  

        studentRepository.deleteById(id);
    }
}