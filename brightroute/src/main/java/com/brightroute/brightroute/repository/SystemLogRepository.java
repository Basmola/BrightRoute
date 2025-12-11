package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.SystemLog;
// import com.brightroute.brightroute.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {

    // البحث عن كل الـ logs الخاصة بـ User معين
    // List<SystemLog> findByUser(User user);

    // البحث عن الـ logs الخاصة بـ User بترتيب زمني تنازلي
    // List<SystemLog> findByUserOrderByTimestampDesc(User user);

    // البحث عن الـ logs حسب نوع الـ action
    List<SystemLog> findByAction(String action);
}
