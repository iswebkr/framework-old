package kr.isweb.sample.web.city.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.isweb.cmmn.util.cmmn.CmmnExcelUtil;
import kr.isweb.cmmn.web.controller.CmmnDefaultController;
import kr.isweb.sample.web.city.dto.CityParamDto;
import kr.isweb.sample.web.city.dto.CityResultDto;
import kr.isweb.sample.web.city.service.CityService;

@Controller
@RequestMapping("/city")
public class CityController extends CmmnDefaultController {

	@Resource(name="cityService")
	CityService<CityParamDto, CityResultDto, String, Object> cityService;

	@GetMapping("/list")
	public ResponseEntity<String> getCityList(@ModelAttribute CityParamDto paramDto, HttpServletRequest request, HttpServletResponse response, Model model) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		List<CityResultDto> list = cityService.getCityList(paramDto);
		return new ResponseEntity<>(gson.toJson(list), HttpStatus.OK);
	}

	@GetMapping("/excel-export")
	public ModelAndView exportCityListToExcel(@ModelAttribute CityParamDto paramDto, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CityResultDto> cityList = cityService.getCityList(paramDto);
		model.addAttribute("dataList", cityList);
		return new ModelAndView("sxssfDownloadView");
	}

	@GetMapping("/excel-read")
	public ResponseEntity<String> excelRead() {
		CmmnExcelUtil excelUtil = new CmmnExcelUtil();
		String str = null;
		str = excelUtil.excelRead(new File("/temp/sample-xlsx-file-for-testing.xlsx"));
		return new ResponseEntity<>(str, HttpStatus.OK);
	}
}
