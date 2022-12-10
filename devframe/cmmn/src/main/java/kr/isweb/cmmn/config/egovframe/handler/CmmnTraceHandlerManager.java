package kr.isweb.cmmn.config.egovframe.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.fdl.cmmn.trace.handler.TraceHandler;
import egovframework.rte.fdl.cmmn.trace.manager.AbstractTraceHandleManager;
import egovframework.rte.fdl.cmmn.trace.manager.TraceHandlerService;

public class CmmnTraceHandlerManager extends AbstractTraceHandleManager implements TraceHandlerService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean trace(Class<?> clazz, String message) {
		if(!enableMatcher()) {
			return false;
		} else {
			for(String pattern : patterns) {
				logger.info("pattern : {}, getPackageName : {}", pattern, getPackageName());
				logger.info("pattern match : {}", pm.match(pattern, getPackageName()));
				if(pm.match(pattern, getPackageName())) {
					for(TraceHandler eh : handlers) {
						logger.info(">>> TRACE HANDLER : {} - SATRT !!", eh.getClass().getCanonicalName());
						eh.todo(clazz, message);
						logger.info(">>> TRACE HANDLER : {} - END !!", eh.getClass().getCanonicalName());
					}
					break;
				}
			}
		}
		return true;
	}
}
