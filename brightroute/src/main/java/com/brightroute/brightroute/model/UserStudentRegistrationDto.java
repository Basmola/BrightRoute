package com.brightroute.brightroute.model;

import com.brightroute.brightroute.enums.Role;
import com.brightroute.brightroute.enums.AccountStatus;

public class UserStudentRegistrationDto {

    // --- USER FIELDS ---
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password; // Raw password
    
    // Default values for new registrations
    private Role role = Role.STUDENT; 
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    
    // --- STUDENT FIELDS ---
    private String nationalId;
    private Long parentNumber;
    private String idType; // e.g., NATIONAL_ID / BIRTH_CERTIFICATE
    private String levelOfEducation;
    private byte[] nationalIdFront;
    private byte[] birthCertificate;



    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public AccountStatus getAccountStatus() { return accountStatus; }
    public void setAccountStatus(AccountStatus accountStatus) { this.accountStatus = accountStatus; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public Long getParentNumber() { return parentNumber; }
    public void setParentNumber(Long parentNumber) { this.parentNumber = parentNumber; }

    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }

    public String getLevelOfEducation() { return levelOfEducation; }
    public void setLevelOfEducation(String levelOfEducation) { this.levelOfEducation = levelOfEducation; }

    public byte[] getNationalIdFront() { return nationalIdFront; }
    public void setNationalIdFront(byte[] nationalIdFront) { this.nationalIdFront = nationalIdFront; }

    public byte[] getBirthCertificate() { return birthCertificate; }
    public void setBirthCertificate(byte[] birthCertificate) { this.birthCertificate = birthCertificate; }
}