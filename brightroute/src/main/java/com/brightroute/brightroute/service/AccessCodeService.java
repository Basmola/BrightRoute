package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.repository.AccessCodeRepository;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import jakarta.persistence.*;

@Service
public class AccessCodeService {

    @Autowired
    private AccessCodeRepository accessCodeRepository;

    public AccessCode createAccessCode(String createdBy) {
        AccessCode code = new AccessCode();
        code.setCode(UUID.randomUUID().toString());
        code.setExpiryDate(LocalDateTime.now().plusDays(7));
        code.setStatus("ACTIVE");
        code.setCreatedBy(createdBy);
        return accessCodeRepository.save(code);
    }

    public boolean validateAccessCode(String code) {
        return accessCodeRepository.findByCode(code)
                .filter(c -> c.getStatus().equals("ACTIVE")
                        && c.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public void revokeAccessCode(Long id) {
        AccessCode code = accessCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AccessCode not found"));
        code.setStatus("REVOKED");
        accessCodeRepository.save(code);
    }
}
