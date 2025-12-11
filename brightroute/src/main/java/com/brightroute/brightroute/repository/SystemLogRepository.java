package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Integer> { 
    // CORRECTION: PK type changed from Long to Integer

    // البحث عن كل الـ logs الخاصة بـ User معين (by User entity or its ID)
    List<SystemLog> findByUser_Id(Integer userId);

    // البحث عن الـ logs الخاصة بـ User بترتيب زمني تنازلي
    List<SystemLog> findByUser_IdOrderByTimestampDesc(Integer userId);

    // البحث عن الـ logs حسب نوع الـ action
    List<SystemLog> findByAction(String action);
}