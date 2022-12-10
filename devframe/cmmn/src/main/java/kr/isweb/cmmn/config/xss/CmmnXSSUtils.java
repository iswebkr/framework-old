package kr.isweb.cmmn.config.xss;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import com.nhncorp.lucy.security.xss.LucyXssFilter;
import com.nhncorp.lucy.security.xss.XssPreventer;
import com.nhncorp.lucy.security.xss.XssSaxFilter;

public class CmmnXSSUtils {
	public static String stripXSS(String input) {
		// XssFilter filter = XssFilter.getInstance("lucy-xss.xml");
		LucyXssFilter filter = XssSaxFilter.getInstance("lucy-xss-sax.xml");
		if(StringUtils.isNotBlank(input)) {
			input = filter.doFilter(input);
			return Jsoup.clean(input, Safelist.none());
		}
		return null;
	}

	public static String escape(String input) {
		return XssPreventer.escape(input);
	}

	public static String unescape(String input) {
		return XssPreventer.unescape(input);
	}
}
