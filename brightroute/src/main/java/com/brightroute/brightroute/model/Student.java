package com.brightroute.brightroute.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Student", schema = "users")  
public class Student {

    @Id
    @Column(name = "student_id")
    private Integer id;  

    @Column(name = "student_national_id", unique = true)
    private String nationalId;

    @Column(name = "parent_number")
    private Long parentNumber;

    @Column(name = "student_id_type") 
    private String idType;  

    @Lob
    @Column(name = "student_national_id_front")
    private byte[] nationalIdFront;

    @Lob
    @Column(name = "student_birth_certificate")
    private byte[] birthCertificate;

    @Column(name = "student_level_of_education")
    private String levelOfEducation;

    @Column(name = "student_created_at", updatable = false)  
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId 
    @JoinColumn(name = "student_id")  
    private User user;

    public Student() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public Long getParentNumber() { return parentNumber; }
    public void setParentNumber(Long parentNumber) { this.parentNumber = parentNumber; }

    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }

    public byte[] getNationalIdFront() { return nationalIdFront; }
    public void setNationalIdFront(byte[] nationalIdFront) { this.nationalIdFront = nationalIdFront; }

    public byte[] getBirthCertificate() { return birthCertificate; }
    public void setBirthCertificate(byte[] birthCertificate) { this.birthCertificate = birthCertificate; }

    public String getLevelOfEducation() { return levelOfEducation; }
    public void setLevelOfEducation(String levelOfEducation) { this.levelOfEducation = levelOfEducation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}