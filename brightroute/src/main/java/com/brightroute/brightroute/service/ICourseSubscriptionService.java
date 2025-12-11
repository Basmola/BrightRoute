package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.CourseSubscription;

public interface ICourseSubscriptionService {

    CourseSubscription subscribe(Long studentId, Long courseId);

    void unsubscribe(Long studentId, Long courseId);
}
