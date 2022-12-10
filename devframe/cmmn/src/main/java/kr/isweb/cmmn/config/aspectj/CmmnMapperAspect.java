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

import kr.isweb.cmmn.config.datasource.router.RoutingContextHolder;
import kr.isweb.cmmn.config.datasource.router.annotation.RoutingMethod;
import kr.isweb.cmmn.config.datasource.router.annotation.RoutingType;

@Aspect
@Component
public class CmmnMapperAspect {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("within(@org.apache.ibatis.annotations.Mapper *)")
	public void mybatisMapper() {}

	@Pointcut("within(@egovframework.rte.psl.dataaccess.mapper.Mapper *)")
	public void egovframeMapper() {}

	@Around("egovframeMapper()")
	public Object queryExecuteTime(ProceedingJoinPoint joinPoint) throws Throwable {
		Object ret = null;
		String fullMethodName = null;
		StopWatch timer = new StopWatch();

		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Class<?> mapperClass = methodSignature.getDeclaringType();
		Method method = methodSignature.getMethod();
		fullMethodName = mapperClass.getCanonicalName() + "." + method.getName();
		RoutingType routingType = mapperClass.getDeclaredAnnotation(RoutingType.class);
		RoutingMethod routingMethod = method.getDeclaredAnnotation(RoutingMethod.class);

		if(logger.isInfoEnabled()) {
			logger.info("#### BEGIN-MAPPER : {} - START", fullMethodName);
		}

		timer.start(fullMethodName);
		if(routingMethod != null) {
			logger.info("Routed : {}", routingMethod.value());
			RoutingContextHolder.set(routingMethod.value());
			ret = joinPoint.proceed();
			RoutingContextHolder.clear();
			logger.info("Route release : {}", routingMethod.value());
		} else if (routingType != null) {
			logger.info("Routed : {}", routingType.value());
			RoutingContextHolder.set(routingType.value());
			ret = joinPoint.proceed();
			RoutingContextHolder.clear();
			logger.info("Route release : {}", routingType.value());
		} else {
			ret = joinPoint.proceed();
		}
		timer.stop();

		if(logger.isInfoEnabled()) {
			logger.info("#### END-MAPPER : {} - END", fullMethodName);
			logger.info(">>> Totaly {} Method Execute Time : {} Second", fullMethodName, Math.round(timer.getTotalTimeSeconds()));
		}

		return ret;
	}
}
