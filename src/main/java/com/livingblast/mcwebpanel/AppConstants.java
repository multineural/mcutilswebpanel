package com.livingblast.mcwebpanel;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class AppConstants {

	public static final String NEWLINE = System.getProperty("line.separator", "/n");
	
	public static SimpleDateFormat getDateFormat() {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
		return DATE_FORMAT;
	}
}
