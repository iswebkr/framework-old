package kr.isweb.cmmn.config.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import egovframework.rte.fdl.cmmn.aspect.ExceptionTransfer;

@Aspect
@Component
public class CmmnServiceExceptionAspect {

	@Autowired
	ExceptionTransfer exceptionTransfer;

	@AfterThrowing(value="within(@org.springframework.stereotype.Service *)", throwing = "ex")
	public void doServiceMethodCall(JoinPoint joinPoint, Exception ex) throws Throwable {
		exceptionTransfer.transfer(joinPoint, ex);
	}
}
