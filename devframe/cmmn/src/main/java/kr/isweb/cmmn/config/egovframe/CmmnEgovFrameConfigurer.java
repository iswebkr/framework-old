package kr.isweb.cmmn.config.egovframe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.util.AntPathMatcher;

import egovframework.rte.fdl.cmmn.aspect.ExceptionTransfer;
import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;
import egovframework.rte.fdl.cmmn.exception.manager.ExceptionHandlerService;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.cmmn.trace.handler.TraceHandler;
import egovframework.rte.fdl.cmmn.trace.manager.TraceHandlerService;
import egovframework.rte.fdl.cryptography.EgovARIACryptoService;
import egovframework.rte.fdl.cryptography.EgovDigestService;
import egovframework.rte.fdl.cryptography.EgovGeneralCryptoService;
import egovframework.rte.fdl.cryptography.EgovPasswordEncoder;
import egovframework.rte.fdl.cryptography.impl.EgovARIACryptoServiceImpl;
import egovframework.rte.fdl.cryptography.impl.EgovDigestServiceImpl;
import egovframework.rte.fdl.cryptography.impl.EgovGeneralCryptoServiceImpl;
import egovframework.rte.fdl.idgnr.impl.EgovUUIdGnrServiceImpl;
import kr.isweb.cmmn.config.egovframe.exception.CmmnExceptionHandleManager;
import kr.isweb.cmmn.config.egovframe.exception.CmmnExceptionHandler;
import kr.isweb.cmmn.config.egovframe.handler.CmmnTraceHandler;
import kr.isweb.cmmn.config.egovframe.handler.CmmnTraceHandlerManager;

public class CmmnEgovFrameConfigurer {

	@Autowired
	Environment env;

	@Bean
	public LeaveaTrace leaveaTrace() {
		LeaveaTrace leaveaTrace = new LeaveaTrace();
		leaveaTrace.setTraceHandlerServices(new TraceHandlerService[] {
				traceHandlerManager()
		});
		return leaveaTrace;
	}

	@Bean
	public ExceptionTransfer exceptionTransfer() {
		ExceptionTransfer exceptionTransfer = new ExceptionTransfer();
		exceptionTransfer.setExceptionHandlerService(new ExceptionHandlerService[] {
				cmmnExceptionHandlerManager()
		});
		return exceptionTransfer;
	}

	@Bean
	@Lazy
	public EgovUUIdGnrServiceImpl egovUUIdGenService() throws Throwable {
		EgovUUIdGnrServiceImpl egovUUIdGenService = new EgovUUIdGnrServiceImpl();
		return egovUUIdGenService;
	}

	/**
	 * https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte2:fdl:encryption_decryption
	 */
	@Bean
	@Lazy
	public EgovPasswordEncoder egovPasswordEncoder() {
		EgovPasswordEncoder egovPasswordEncoder = new EgovPasswordEncoder();
		egovPasswordEncoder.setAlgorithm(env.getProperty("crypto.password.algorithm"));
		egovPasswordEncoder.setHashedPassword(env.getProperty("crypto.hashed.password"));
		return egovPasswordEncoder;
	}

	/**
	 * https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte2:fdl:encryption_decryption
	 */
	@Bean
	@Lazy
	public EgovARIACryptoService ariaCryptoService() {
		EgovARIACryptoService ariaCryptoService = new EgovARIACryptoServiceImpl();
		ariaCryptoService.setPasswordEncoder(egovPasswordEncoder());
		ariaCryptoService.setBlockSize(1024);
		return ariaCryptoService;
	}

	/**
	 * https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte2:fdl:encryption_decryption
	 */
	@Bean
	@Lazy
	public EgovDigestService egovDigestService() {
		EgovDigestService egovDigestService = new EgovDigestServiceImpl();
		egovDigestService.setAlgorithm(env.getProperty("crypto.password.algorithm"));
		egovDigestService.setPlainDigest(Boolean.FALSE);
		return egovDigestService;
	}

	/**
	 * https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte2:fdl:encryption_decryption
	 */
	@Bean
	@Lazy
	public EgovGeneralCryptoService egovGeneralCryptoService() {
		EgovGeneralCryptoService egovGeneralCryptoService = new  EgovGeneralCryptoServiceImpl();
		egovGeneralCryptoService.setPasswordEncoder(egovPasswordEncoder());
		egovGeneralCryptoService.setAlgorithm("PBEWithSHA1AndDESede");
		egovGeneralCryptoService.setBlockSize(1024);
		return egovGeneralCryptoService;
	}

	private TraceHandlerService traceHandlerManager() {
		CmmnTraceHandlerManager cmmnTraceHandlerManager = new CmmnTraceHandlerManager();
		cmmnTraceHandlerManager.setReqExpMatcher(new AntPathMatcher());
		cmmnTraceHandlerManager.setPatterns(new String[] {
				"*"
		});
		cmmnTraceHandlerManager.setHandlers(new TraceHandler[] {
				cmmnTraceHandler()
		});
		return cmmnTraceHandlerManager;
	}

	private TraceHandler cmmnTraceHandler() {
		CmmnTraceHandler cmmnTraceHandler = new CmmnTraceHandler();
		return cmmnTraceHandler;
	}

	private ExceptionHandlerService cmmnExceptionHandlerManager() {
		CmmnExceptionHandleManager cmmnExceptionHandlerManager = new CmmnExceptionHandleManager();
		cmmnExceptionHandlerManager.setPatterns(new String[] {
				"**service.**Impl.*"
		});
		cmmnExceptionHandlerManager.setHandlers(new ExceptionHandler[] {
				cmmnExceptionHandler()
		});
		return cmmnExceptionHandlerManager;
	}

	private ExceptionHandler cmmnExceptionHandler() {
		ExceptionHandler exceptionHandler = new CmmnExceptionHandler();
		return exceptionHandler;
	}
}
