package kr.isweb.sample.web.city.mapper;

import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface SampleMapper {
	public int getCount(Map<String, Object> map);
}
