package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.SystemLog;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    List<SystemLog> findByUserId(Long userId);
}
