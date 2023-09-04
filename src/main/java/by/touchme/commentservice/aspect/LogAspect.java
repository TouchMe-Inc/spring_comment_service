package by.touchme.commentservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Pointcut("execution(* by.touchme.commentservice.controller.*.*(..))")
    protected void controller() {
    }

    @Around("controller()")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        log.info("Request {} started with parameters: {}", signature, obtainParameters(joinPoint));

        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            log.info("{} failed with exception message: {}", signature, e.getMessage());
            throw e;
        }

        log.info("{} finished with return value: {}", signature, proceed);
        return proceed;
    }

    private Map<String, Object> obtainParameters(ProceedingJoinPoint joinPoint) {
        Map<String, Object> parameters = new HashMap<>();
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length && i < parameterValues.length; i++) {
            parameters.put(parameterNames[i], parameterValues[i]);
        }

        return parameters;
    }
}
