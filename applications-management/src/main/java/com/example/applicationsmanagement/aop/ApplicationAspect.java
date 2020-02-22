package com.example.applicationsmanagement.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApplicationAspect {

    private static final Logger logger = Logger.getLogger(ApplicationAspect.class);

    private long startTime = 0;
    private long timeTaken = 0;

    @Before("execution(* com.example.applicationsmanagement.Service.AppService.*(..))")
    public void before(JoinPoint joinPoint) {
        this.startTime = System.currentTimeMillis();
        logger.info("Before");
        logger.info(joinPoint.getSignature().getName());

    }

    @AfterReturning(pointcut = "execution(* com.example.applicationsmanagement.Service.AppService.*(..))",
            returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        this.timeTaken = System.currentTimeMillis() - startTime;
        logger.info(joinPoint.getSignature().getName() + " Returned with " + result.toString());
        logger.info("Time Taken by " + joinPoint.getSignature().getName() +  " is " +  timeTaken + "ms");
       if(result==null){
            logger.error("Error at "+joinPoint.getSignature().getName());
        }
    }
}
