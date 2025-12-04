package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.AccessCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccessCodeRepository extends JpaRepository<AccessCode, Long> {
    Optional<AccessCode> findByCodeValue(String codeValue);
}
