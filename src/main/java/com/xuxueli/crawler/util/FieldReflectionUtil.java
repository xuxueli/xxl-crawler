package com.xuxueli.crawler.util;

import com.xuxueli.crawler.annotation.PageFieldSelect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * api request field, reflect util
 * @author xuxueli 2017-05-26
 */
public final class FieldReflectionUtil {

	private FieldReflectionUtil(){}

	public static Byte parseByte(String value) {
		try {
			value = value.replaceAll("　", "");
			return Byte.valueOf(value);
		} catch(NumberFormatException e) {
			throw new RuntimeException("parseByte but input illegal input=" + value, e);
		}
	}

	public static Boolean parseBoolean(String value) {
		value = value.replaceAll("　", "");
		if (Boolean.TRUE.toString().equalsIgnoreCase(value)) {
			return Boolean.TRUE;
		} else if (Boolean.FALSE.toString().equalsIgnoreCase(value)) {
			return Boolean.FALSE;
		} else {
			throw new RuntimeException("parseBoolean but input illegal input=" + value);
		}
	}

	public static Integer parseInt(String value) {
		try {	
			value = value.replaceAll("　", "");
			return Integer.valueOf(value);
		} catch(NumberFormatException e) {
			throw new RuntimeException("parseInt but input illegal input=" + value, e);
		}
	}

	public static Short parseShort(String value) {
		try {
			value = value.replaceAll("　", "");
			return Short.valueOf(value);
		} catch(NumberFormatException e) {
			throw new RuntimeException("parseShort but input illegal input=" + value, e);
		}
	}

	public static Long parseLong(String value) {
		try {
			value = value.replaceAll("　", "");
			return Long.valueOf(value);
		} catch(NumberFormatException e) {
			throw new RuntimeException("parseLong but input illegal input=" + value, e);
		}
	}

	public static Float parseFloat(String value) {
		try {
			value = value.replaceAll("　", "");
			return Float.valueOf(value);
		} catch(NumberFormatException e) {
			throw new RuntimeException("parseFloat but input illegal input=" + value, e);
		}
	}

	public static Double parseDouble(String value) {
		try {
			value = value.replaceAll("　", "");
			return Double.valueOf(value);
		} catch(NumberFormatException e) {
			throw new RuntimeException("parseDouble but input illegal input=" + value, e);
		}
	}

	public static Date parseDate(PageFieldSelect apiRequestParam, String value) {
		try {
			String datePattern = "yyyy-MM-dd HH:mm:ss";
			if (apiRequestParam != null) {
				datePattern = apiRequestParam.datePattern();
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			return dateFormat.parse(value);
		} catch(ParseException e) {
			throw new RuntimeException("parseDate but input illegal input=" + value, e);
		}
	}

	/**
	 * 参数解析 （支持：Byte、Boolean、String、Short、Integer、Long、Float、Double、Date）
	 *
	 * @param field
	 * @param value
	 * @return Object
	 */
	public static Object parseValue(Field field, String value) {

		Class<?> fieldType = field.getType();

		// parse list item
		if (field.getGenericType() instanceof ParameterizedType) {
			ParameterizedType fieldGenericType = (ParameterizedType) field.getGenericType();
			if (fieldGenericType.getRawType().equals(List.class)) {
				Type gtATA = fieldGenericType.getActualTypeArguments()[0];
				fieldType = (Class<?>) gtATA;
			}
		}

		PageFieldSelect apiRequestParam = field.getAnnotation(PageFieldSelect.class);

		if(value==null || value.trim().length()==0)
			return null;
		value = value.trim();

		 if (Byte.class.equals(fieldType) || Byte.TYPE.equals(fieldType)) {
			return parseByte(value);
		} else if (Boolean.class.equals(fieldType) || Boolean.TYPE.equals(fieldType)) {
			return parseBoolean(value);
		}/* else if (Character.class.equals(fieldType) || Character.TYPE.equals(fieldType)) {
			 return value.toCharArray()[0];
		}*/ else if (String.class.equals(fieldType)) {
			return value;
		} else if (Short.class.equals(fieldType) || Short.TYPE.equals(fieldType)) {
			 return parseShort(value);
		} else if (Integer.class.equals(fieldType) || Integer.TYPE.equals(fieldType)) {
			return parseInt(value);
		} else if (Long.class.equals(fieldType) || Long.TYPE.equals(fieldType)) {
			return parseLong(value);
		} else if (Float.class.equals(fieldType) || Float.TYPE.equals(fieldType)) {
			return parseFloat(value);
		} else if (Double.class.equals(fieldType) || Double.TYPE.equals(fieldType)) {
			return parseDouble(value);
		} else if (Date.class.equals(fieldType)) {
			 return parseDate(apiRequestParam, value);

		} else {
			throw new RuntimeException("request illeagal type, type must be Integer not int Long not long etc, type=" + fieldType);
		}
	}

}