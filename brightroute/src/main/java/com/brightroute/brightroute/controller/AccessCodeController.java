package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.AccessCode;
import com.brightroute.brightroute.service.AccessCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/access-codes")
public class AccessCodeController {

    @Autowired
    private AccessCodeService accessCodeService;

    // 1. إنشاء كود وصول جديد
    @PostMapping
    public ResponseEntity<AccessCode> create(
            @RequestParam Integer courseId,
            @RequestParam(required = false) Integer lectureId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String codeValue) {
        AccessCode accessCode = accessCodeService.createAccessCode(courseId, lectureId, userId, codeValue);
        return ResponseEntity.ok(accessCode);
    }

    // 2. التحقق من صلاحية الكود (لا يقوم بتغيير حالة الكود)
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestParam String code) {
        boolean isValid = accessCodeService.validateAccessCode(code);
        return ResponseEntity.ok(isValid);
    }

    // 3. استرداد/استخدام الكود (Redeem)
    // هذا هو الإجراء الذي يقوم بربط الكود بالمحاضرة والمستخدم وإلغاء صلاحيته.
    @PostMapping("/redeem")
    public ResponseEntity<AccessCode> redeem(
            @RequestParam String codeValue,
            @RequestParam Integer userId, // المستخدم الذي يقوم بالاسترداد (إلزامي)
            @RequestParam(required = false) Integer lectureId // المحاضرة التي يستخدم فيها الكود (اختياري)
    ) {
        AccessCode redeemedCode = accessCodeService.redeemAccessCode(codeValue, userId, lectureId);
        return ResponseEntity.ok(redeemedCode);
    }

    // 4. إلغاء/حذف الكود (Revoke) - (قد يعني "إلغاء تنشيطه يدوياً قبل الاستخدام")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Integer id) {
        accessCodeService.revokeAccessCode(id);
        return ResponseEntity.noContent().build();
    }

    // 5. Get All Access Codes
    @GetMapping
    public ResponseEntity<List<AccessCode>> getAll() {
        return ResponseEntity.ok(accessCodeService.getAllAccessCodes());
    }
}