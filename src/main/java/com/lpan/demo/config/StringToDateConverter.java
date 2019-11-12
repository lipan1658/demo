package com.lpan.demo.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class StringToDateConverter implements Converter<String , Date> {

	@Override
	public Date convert(String source) {
		if(StringUtils.isEmpty(source)) {
			return null;
		}
		if(source.contains(":")) {
			return parseDateTime(source);
		}else if(source.length()==10){
			return parseDate(source);
		}else {
			throw new IllegalArgumentException("日期格式非法");
		}
	}
	
	private Date parseDateTime(String str) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mi:ss");
		Date date = null;
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
		
	}
	private Date parseDate(String str) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
		
	}


}
