package com.github.fullautomation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	static String configFile = "src/main/resources/config.properties";
	
	public static String getValue(String key) throws IOException {
		String value = null;
		InputStream inStream = new FileInputStream(new File(configFile));
		Properties properties = new Properties();
		properties.load(inStream);
		value = properties.getProperty(key, null);
		value = System.getProperty(key, value);
		return value;
	}
	
}
