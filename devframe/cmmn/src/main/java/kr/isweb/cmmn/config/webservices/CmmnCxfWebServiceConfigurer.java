package kr.isweb.cmmn.config.webservices;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.springframework.context.annotation.Bean;

import kr.isweb.cmmn.web.service.cxf.sample.CmmnSampleCxfWebServiceImpl;
import kr.isweb.cmmn.web.service.cxf.sample.file.CmmnSampleCxfFileUploadImpl;

public class CmmnCxfWebServiceConfigurer {
	@Bean(name = Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		return new SpringBus();
	}

	@Bean
	public Endpoint cmmnCxfWebServiceSampleEndpoint() {
		Endpoint endpoint = Endpoint.publish("/CxfSampleWebService", new CmmnSampleCxfWebServiceImpl());
		return endpoint;
	}

	@Bean
	public Endpoint cmmnCxfWebServiceFileUploadEndpoint() {
		Endpoint endpoint = Endpoint.publish("/CxfSampleFileUpload", new CmmnSampleCxfFileUploadImpl());
		SOAPBinding binding = (SOAPBinding) endpoint.getBinding();
		binding.setMTOMEnabled(Boolean.TRUE);
		return endpoint;
	}
}
