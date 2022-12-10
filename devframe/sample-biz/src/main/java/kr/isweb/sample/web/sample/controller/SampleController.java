package kr.isweb.sample.web.sample.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import kr.isweb.cmmn.web.controller.CmmnDefaultController;
import kr.isweb.sample.web.sample.dto.SampleDto;

@Controller
@RequestMapping({"", "/"})
public class SampleController extends CmmnDefaultController {

	@GetMapping
	public ModelAndView sample(HttpServletRequest request, HttpServletResponse response, Model model) {
		return pjaxView(request, response, "sample");
	}

	@GetMapping("/sample-one")
	public ModelAndView sampleOne(HttpServletRequest request, HttpServletResponse response, Model model) {
		return pjaxView(request, response, "sample-one");
	}

	@PostMapping
	public ResponseEntity<String> sampleDo(@Valid SampleDto sampleDto, Model model) {
		return ResponseEntity.ok("success");
	}
}
