package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.service.AccessCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access-codes")
public class AccessCodeController {

    @Autowired
    private AccessCodeService accessCodeService;
    // إنشاء كود وصول جديد  
    @PostMapping
    public ResponseEntity<AccessCode> create(
            // CORRECT: Changed all Long IDs to Integer
            @RequestParam Integer courseId,
            @RequestParam(required = false) Integer lectureId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String codeValue
    ) 
    {
        AccessCode accessCode = accessCodeService.createAccessCode(courseId, lectureId, userId, codeValue);
        return ResponseEntity.ok(accessCode);
    }

    // التحقق من صلاحية الكود
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestParam String code) {
        boolean isValid = accessCodeService.validateAccessCode(code);
        return ResponseEntity.ok(isValid);
    }

    // إلغاء/حذف الكود
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Integer id) { // CORRECT: ID is Integer
        accessCodeService.revokeAccessCode(id);
        return ResponseEntity.noContent().build();
    }
}