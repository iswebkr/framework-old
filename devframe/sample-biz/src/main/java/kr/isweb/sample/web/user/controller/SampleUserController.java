package kr.isweb.sample.web.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;

import kr.isweb.cmmn.config.jwt.CmmnJwtTokenProvider;
import kr.isweb.cmmn.config.jwt.dto.CmmnJwtToken;
import kr.isweb.cmmn.web.controller.CmmnDefaultController;

@Controller
@RequestMapping("/user")
public class SampleUserController extends CmmnDefaultController {

	@Autowired
	CmmnJwtTokenProvider cmmnJwtTokenProvider;

	@PostMapping("/authentication")
	public ResponseEntity<?> auth() {
		Gson gson = new Gson();
		CmmnJwtToken jwtToken = cmmnJwtTokenProvider.makeToken("iswebkr@gmail.com");
		return ResponseEntity.ok(gson.toJson(jwtToken));
	}


	@PostMapping("/re-authentication")
	public ResponseEntity<?> reauth() {
		Gson gson = new Gson();
		CmmnJwtToken jwtToken = cmmnJwtTokenProvider.makeRefreshToken();
		return ResponseEntity.ok(gson.toJson(jwtToken));
	}
}
