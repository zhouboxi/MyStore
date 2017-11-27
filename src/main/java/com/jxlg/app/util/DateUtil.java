package com.jxlg.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateUtil implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		SimpleDateFormat[] sdfs={
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
				new SimpleDateFormat("yyyy/MM/dd"),
				new SimpleDateFormat("yyyy-MM-dd")
				};
		Date date=null;
		
		for (SimpleDateFormat simpleDateFormat : sdfs) {
			try {
				date=simpleDateFormat.parse(source);
			} catch (Exception e) {
				// TODO: handle exception
				continue;
			}
			
		}
		return date;
	}

}
