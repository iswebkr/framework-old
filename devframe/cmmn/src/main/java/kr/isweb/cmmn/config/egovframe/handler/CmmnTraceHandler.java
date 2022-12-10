package kr.isweb.cmmn.config.egovframe.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.fdl.cmmn.trace.handler.TraceHandler;

public class CmmnTraceHandler implements TraceHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void todo(Class<?> clazz, String message) {
		logger.info("비지니스로직 처리 후 처리 로직 추가");
	}
}
