package kr.isweb.cmmn.config.message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import kr.isweb.cmmn.config.message.mapper.CmmnMessageMapper;

public class CmmnRelodableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	@Autowired
	CmmnMessageMapper cmmnMessageMapper;

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		String message = "";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("msgId", code);
		paramMap.put("msgLocale", locale.getLanguage().toUpperCase());
		message = cmmnMessageMapper.getMessage(paramMap);
		return message;
	}
}
