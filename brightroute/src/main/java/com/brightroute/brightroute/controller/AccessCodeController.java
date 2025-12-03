package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.service.AccessCodeService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import jakarta.persistence.*;

@RestController
@RequestMapping("/api/access-codes")
public class AccessCodeController {
    @Autowired
    private AccessCodeService accessCodeService;

    @PostMapping
    public AccessCode create(@RequestParam String createdBy) {
        return accessCodeService.createAccessCode(createdBy);
    }

    @GetMapping("/validate")
    public boolean validate(@RequestParam String code) {
        return accessCodeService.validateAccessCode(code);
    }

    @DeleteMapping("/{id}")
    public void revoke(@PathVariable Long id) {
        accessCodeService.revokeAccessCode(id);
    }
}

