package kr.isweb.cmmn.config.egovframe.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;
import egovframework.rte.fdl.cmmn.exception.manager.AbstractExceptionHandleManager;
import egovframework.rte.fdl.cmmn.exception.manager.ExceptionHandlerService;

public class CmmnExceptionHandleManager extends AbstractExceptionHandleManager implements ExceptionHandlerService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean run(Exception exception) throws Exception {
		if(!enableMatcher()) {
			return false;
		}

		for(String pattern : patterns) {
			logger.info("pattern : {}, thisPackageName : {}", pattern, thisPackageName);
			logger.info("pattern match : {}", pm.match(pattern, thisPackageName));
			if(pm.match(pattern, thisPackageName)) {
				for(ExceptionHandler eh : handlers) {
					logger.info(">>> EXCEPTION HANDLER : {} - SATRT !!", eh.getClass().getCanonicalName());
					eh.occur(exception, getPackageName());
					logger.info(">>> EXCEPTION HANDLER : {} - END !!", eh.getClass().getCanonicalName());
				}
				break;
			}
		}

		return true;
	}
}
