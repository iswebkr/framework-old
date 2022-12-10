package kr.isweb.sample.web.city.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.idgnr.impl.EgovUUIdGnrServiceImpl;
import kr.isweb.cmmn.web.service.CmmnAbstractServiceImpl;
import kr.isweb.sample.web.city.dto.CityParamDto;
import kr.isweb.sample.web.city.dto.CityResultDto;
import kr.isweb.sample.web.city.mapper.CityMapper;
import kr.isweb.sample.web.city.mapper.SampleMapper;

@Service("cityService")
public class CityServiceImpl extends CmmnAbstractServiceImpl implements CityService<CityParamDto, CityResultDto, String, Object> {

	@Resource(name="cityMapper")
	CityMapper<CityParamDto, CityResultDto, String, Object> cityMapper;

	@Resource(name="sampleMapper")
	SampleMapper sampleMapper;

	@Resource(name="egovUUIdGenService")
	EgovUUIdGnrServiceImpl egovUUIdGenService;

	@Override
	public Map<String, Object> getPaginatedCityList(CityParamDto paramDto) {
		Map<String, Object> map = new HashMap<>();
		List<CityResultDto> list = new ArrayList<>();
		try {
			int count = cityMapper.selectCityCount(paramDto);
			list = cityMapper.selecgPaginatedCityList(paramDto);
			map.put("count", count);
			map.put("list", list);
		} catch (Exception e) {
			leaveaTrace("sample.message.key");
		}
		return map;
	}

	@Override
	public List<CityResultDto> getCityList(CityParamDto paramDto) {
		List<CityResultDto> list = cityMapper.selecgPaginatedCityList(paramDto);
		return list;
	}

	@Override
	public CityResultDto getCity(CityParamDto paramDto) {
		return cityMapper.selectCity(paramDto);
	}

	@Override
	public int insertCity(CityParamDto paramDto) {
		return cityMapper.insertCity(paramDto);
	}

	@Override
	public int updateCity(CityParamDto paramDto) {
		return cityMapper.updateCity(paramDto);
	}

	@Override
	public int deleteCity(CityParamDto paramDto) {
		return cityMapper.deleteCity(paramDto);
	}
}
