package kr.isweb.cmmn.web.service.cxf.sample;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import egovframework.rte.fdl.cmmn.exception.EgovBizException;

@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@WebService(endpointInterface = "kr.isweb.cmmn.web.service.cxf.sample.CmmnSampleCxfWebService")
public class CmmnSampleCxfWebServiceImpl implements CmmnSampleCxfWebService {
	@Override
	public String helloCxf(String name) throws EgovBizException {
		return "Hello + " + name;
	}
}
