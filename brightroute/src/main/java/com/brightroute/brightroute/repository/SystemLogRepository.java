package com.brightroute.brightroute.repository;

import com.brightroute.brightroute.model.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Integer> { 

    List<SystemLog> findByUser_Id(Integer userId);

    List<SystemLog> findByUser_IdOrderByTimestampDesc(Integer userId);

    List<SystemLog> findByAction(String action);
}