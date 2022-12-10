package kr.isweb.cmmn.config.aspectj;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class CmmnServiceAspect {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void serviceClassMethod() {};

	@Around("serviceClassMethod()")
	public Object doServiceMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
		Object ret = null;
		String fullMethodName = null;
		StopWatch timer = new StopWatch();

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Class<?> mapperClass = methodSignature.getDeclaringType();
		Method method = methodSignature.getMethod();
		fullMethodName = mapperClass.getCanonicalName() + "." + method.getName();

		if(logger.isInfoEnabled()) {
			logger.info("#### BEGIN-SERVICE : {} - START", fullMethodName);
		}

		timer.start(fullMethodName);
		ret = joinPoint.proceed();
		timer.stop();

		if(logger.isInfoEnabled()) {
			logger.info("#### END-SERVICE : {} - END", fullMethodName);
			logger.info(">>> Totaly {} Method Execute Time : {} Second", fullMethodName, Math.round(timer.getTotalTimeSeconds()));
		}

		return ret;
	}
}
