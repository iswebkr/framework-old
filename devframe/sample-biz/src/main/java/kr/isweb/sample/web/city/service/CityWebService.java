package kr.isweb.sample.web.city.service;

import java.util.List;

import javax.jws.WebService;

@WebService
public interface CityWebService<P, R> {
	public List<R> getCityList(P paramDto);
}
