package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.CourseSubscription;
import com.brightroute.brightroute.service.CourseSubscriptionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course-subscription")
public class CourseSubscriptionController {

    private final CourseSubscriptionService courseSubscriptionService;

    public CourseSubscriptionController(CourseSubscriptionService courseSubscriptionService) {
        this.courseSubscriptionService = courseSubscriptionService;
    }

    @PostMapping("/subscribe")
    public CourseSubscription subscribe(
            @RequestParam Long studentId,
            @RequestParam Long courseId
    ) {
        return courseSubscriptionService.subscribe(studentId, courseId);
    }

    @DeleteMapping("/unsubscribe")
    public void unsubscribe(
            @RequestParam Long studentId,
            @RequestParam Long courseId
    ) {
        courseSubscriptionService.unsubscribe(studentId, courseId);
    }
}
