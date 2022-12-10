package kr.isweb.cmmn.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;

public class CmmnDefaultController {
	@Autowired
	protected LeaveaTrace leaveaTrace;

	public ModelAndView pjaxView(HttpServletRequest request, HttpServletResponse response, String view) {
		ModelAndView mav = new ModelAndView();
		if("true".equals(request.getHeader("X-PJAX"))) {
			mav = new ModelAndView(view);
		} else {
			mav = new ModelAndView("adminlte.sample", "view", view);
		}
		return mav;
	}
}
