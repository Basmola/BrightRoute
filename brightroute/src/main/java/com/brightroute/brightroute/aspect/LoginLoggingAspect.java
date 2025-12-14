package com.brightroute.brightroute.aspect;

import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.service.SystemLogService;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginLoggingAspect {

    @Autowired
    private SystemLogService systemLogService;

    @AfterReturning(pointcut = "execution(* com.brightroute.brightroute.service.UserService.login(..))", returning = "result")
    public void logLogin(JoinPoint joinPoint, Object result) {
        if (result instanceof User) {
            User user = (User) result;
            String details = "User logged in successfully (Logged via AOP)";
            systemLogService.logAction("LOGIN", user.getId(), details);
        }
    }
}
