package kr.isweb.cmmn.config.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import egovframework.rte.fdl.cmmn.exception.BaseException;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import egovframework.rte.fdl.cmmn.exception.FdlException;
import egovframework.rte.ptl.mvc.bind.exception.AbstractAnnotationExceptionHandler;

@ControllerAdvice
@Order(2)
public class CmmnAnnotationExceptionHandler extends AbstractAnnotationExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e) {
		e.printStackTrace();
		if(logger.isErrorEnabled()) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	@Override
	public ModelAndView handleRuntimeException(RuntimeException e) {
		e.printStackTrace();
		if(logger.isErrorEnabled()) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	@Override
	public ModelAndView handleBaseException(BaseException e) {
		if(logger.isErrorEnabled()) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	@Override
	public ModelAndView handleEgovBizException(EgovBizException e) {
		if(logger.isErrorEnabled()) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	@Override
	public ModelAndView handleFdlException(FdlException e) {
		if(logger.isErrorEnabled()) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({NoHandlerFoundException.class})
	public ModelAndView handlerNotFoundException(Exception e) {
		if(logger.isErrorEnabled()) {
			logger.error(e.getLocalizedMessage());
		}
		return new ModelAndView("/404");
	}
}
