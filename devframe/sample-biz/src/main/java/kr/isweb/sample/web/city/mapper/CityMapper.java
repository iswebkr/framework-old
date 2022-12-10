package kr.isweb.sample.web.city.mapper;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface CityMapper<P, R, K, V> {
	public int selectCityCount(P paramDto);
	public List<R> selectCityList(P paramDto);
	public List<R> selecgPaginatedCityList(P paramDto);
	public R selectCity(P paramDto);
	public int insertCity(P paramDto);
	public int updateCity(P paramDto);
	public int deleteCity(P paramDto);
}
