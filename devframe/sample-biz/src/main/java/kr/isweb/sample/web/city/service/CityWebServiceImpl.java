package kr.isweb.sample.web.city.service;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import kr.isweb.sample.web.city.dto.CityParamDto;
import kr.isweb.sample.web.city.dto.CityResultDto;
import kr.isweb.sample.web.city.mapper.CityMapper;

@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@WebService(endpointInterface = "kr.isweb.sample.web.city.service.CityWebService")
public class CityWebServiceImpl implements CityWebService<CityParamDto, CityResultDto> {

	@Resource(name="cityMapper")
	CityMapper<CityParamDto, CityResultDto, String, Object> cityMapper;

	@WebMethod
	@Override
	public List<CityResultDto> getCityList(@WebParam CityParamDto paramDto) {
		return cityMapper.selectCityList(paramDto);
	}
}
