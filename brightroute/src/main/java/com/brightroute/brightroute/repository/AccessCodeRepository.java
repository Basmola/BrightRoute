package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.AccessCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccessCodeRepository extends JpaRepository<AccessCode, Integer> { 
    // CORRECT: PK type is Integer
    Optional<AccessCode> findByCodeValue(String codeValue);
}