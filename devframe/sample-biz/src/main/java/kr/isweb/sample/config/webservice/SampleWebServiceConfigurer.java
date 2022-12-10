package kr.isweb.sample.config.webservice;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.bus.spring.SpringBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kr.isweb.cmmn.config.webservices.annotation.EnableApacheCXF;
import kr.isweb.sample.web.city.service.CityWebServiceImpl;

@Configuration
@EnableApacheCXF
public class SampleWebServiceConfigurer {
	@Bean
	public Endpoint cityWebServiceEndPoint(SpringBus cxf) {
		Endpoint endpoint = Endpoint.publish("/cityWebservice", new CityWebServiceImpl());
		SOAPBinding binding = (SOAPBinding) endpoint.getBinding();
		binding.setMTOMEnabled(Boolean.TRUE);
		return endpoint;
	}
}
