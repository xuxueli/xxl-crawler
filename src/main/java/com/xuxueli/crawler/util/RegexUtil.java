package com.xuxueli.crawler.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * regex tool
 *
 * @author xuxueli 2015-5-12 23:57:48
 */
public class RegexUtil {

	/**
	 * 正则匹配
	 * @param regex	: 正则表达式
	 * @param str	: 待匹配字符串
	 * @return boolean
	 */
	public static boolean matches(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	private static final String URL_REGEX = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * url格式校验
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isUrl(String str) {
		if (str==null || str.trim().length()==0) {
			return false;
		}
		return matches(URL_REGEX, str);
	}

}