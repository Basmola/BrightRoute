package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.AccessCode;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessCodeRepository extends JpaRepository<AccessCode, Long> {
    Optional<AccessCode> findByCode(String code);
}

