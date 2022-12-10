package kr.isweb.cmmn.web.service.cxf.sample;

import javax.jws.WebService;

import egovframework.rte.fdl.cmmn.exception.EgovBizException;

@WebService
public interface CmmnSampleCxfWebService {
	public String helloCxf(String name) throws EgovBizException;
}
