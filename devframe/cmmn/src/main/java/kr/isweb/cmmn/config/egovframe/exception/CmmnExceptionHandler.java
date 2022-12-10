package kr.isweb.cmmn.config.egovframe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;

public class CmmnExceptionHandler implements ExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void occur(Exception exception, String packageName) {
		logger.error("서비스 에러 발생시 후처리 로직 추가");
	}
}
