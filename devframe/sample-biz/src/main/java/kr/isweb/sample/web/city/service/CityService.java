package kr.isweb.sample.web.city.service;

import java.util.List;
import java.util.Map;

public interface CityService<P, R, K, V> {
	public Map<K, V> getPaginatedCityList(P paramDto);
	public List<R> getCityList(P paramDto);
	public R getCity(P paramDto);
	public int insertCity(P paramDto);
	public int updateCity(P paramDto);
	public int deleteCity(P paramDto);
}
