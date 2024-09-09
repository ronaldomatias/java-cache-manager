package br.com.ronaldomatias.cachemanager.util;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesUtil {

	private static final Properties properties = new Properties();

	static {
		try {
			InputStream input = ApplicationPropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
			if (input == null) {
				throw new CacheManagerException("application.properties file not found.", null);
			}
			properties.load(input);
		} catch (IOException ex) {
			throw new CacheManagerException("Failed to load application.properties.", ex);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

}