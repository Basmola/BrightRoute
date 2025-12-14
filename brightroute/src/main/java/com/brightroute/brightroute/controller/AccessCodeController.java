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

    @PostMapping
    public ResponseEntity<AccessCode> create(
            @RequestParam Integer courseId,
            @RequestParam(required = false) Integer lectureId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String codeValue) {
        AccessCode accessCode = accessCodeService.createAccessCode(courseId, lectureId, userId, codeValue);
        return ResponseEntity.ok(accessCode);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestParam String code) {
        boolean isValid = accessCodeService.validateAccessCode(code);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/redeem")
    public ResponseEntity<AccessCode> redeem(
            @RequestParam String codeValue,
            @RequestParam Integer userId,  
            @RequestParam(required = false) Integer lectureId  
    ) {
        AccessCode redeemedCode = accessCodeService.redeemAccessCode(codeValue, userId, lectureId);
        return ResponseEntity.ok(redeemedCode);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revoke(@PathVariable Integer id) {
        accessCodeService.revokeAccessCode(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AccessCode>> getAll() {
        return ResponseEntity.ok(accessCodeService.getAllAccessCodes());
    }
}