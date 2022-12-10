package kr.isweb.cmmn.config.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.google.gson.Gson;

public class CmmnAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String errorMessage = null;
		Map<String, Object> map = new HashMap<>();
		Gson gson = new Gson();

		if(exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
			errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
		} else if (exception instanceof AccountExpiredException) {
			errorMessage = "계정 유효기간이 만료되었습니다.<br />관리자에게 문의하시기 바랍니다";
		} else if (exception instanceof CredentialsExpiredException) {
			errorMessage = "비밀번호 유효기간이 만료되었습니다.<br />관리자에게 문의하시기 바랍니다";
		} else if (exception instanceof DisabledException) {
			errorMessage = "계정이 비활성화 되어있습니다.<br />관리자에게 문의하시기 바랍니다";
		} else if (exception instanceof LockedException) {
			errorMessage = "계정이 잠겨있습니다.<br />관리자에게 문의하시기 바랍니다.";
		} else if (exception instanceof UsernameNotFoundException) {
			errorMessage = "일치하는 사용자 정보가 없습니다.<br />회원가입 여부를 확인하신 후 이용하시기 바랍니다.";
		} else {
			errorMessage = "로그인에 실패하였습니다.<br />관리자에게 문의하시기 바랍니다.";
		}

		response.setHeader("Content-Type",  "application/json");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		map.put("success", "false");
		map.put("timestamp",  Calendar.getInstance().getTime());
		map.put("exception",  exception.getLocalizedMessage());
		map.put("error", errorMessage);

		PrintWriter out = response.getWriter();
		out.print(gson.toJson(map));
		out.flush();
		out.close();
	}

}
