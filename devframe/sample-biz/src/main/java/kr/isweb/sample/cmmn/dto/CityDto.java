package kr.isweb.sample.cmmn.dto;

import java.io.Serializable;

import kr.isweb.cmmn.web.dto.CmmnDefaultDto;

public class CityDto extends CmmnDefaultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private String countryCode;

	private String district;

	private int population;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}
}
