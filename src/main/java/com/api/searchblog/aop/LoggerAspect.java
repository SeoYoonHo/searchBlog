package com.api.searchblog.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LoggerAspect {
    @Pointcut("within(com.api.searchblog.controller..*)")
    public void cut() { }

    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.debug("======= method name = {} =======", method.getName());

        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) log.info("no parameter");
        for (Object arg : args) {
            log.debug("parameter type = {}", arg.getClass().getSimpleName());
            log.debug("parameter value = {}", arg);
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        Method method = getMethod(joinPoint);
        log.debug("======= method name = {} =======", method.getName());

        log.debug("return type = {}", returnObj.getClass().getSimpleName());
        log.debug("return value = {}", returnObj);
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

}
