package com.lpan.demo.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class StringToDateConverter implements Converter<String , Date> {

	@Override
	public Date convert(String source) {
		if(source.contains(":")) {
			return parseDateTime(source);
		}else {
			return parseDate(source);
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
