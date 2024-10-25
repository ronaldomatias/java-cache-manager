package br.com.ronaldomatias.cachemanager.util;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesUtils {

	private static final Properties properties = new Properties();

	static {
		try (InputStream input = ApplicationPropertiesUtils.class.getClassLoader().getResourceAsStream("cache-manager.properties")) {
			if (input == null) {
				throw new CacheManagerException("cache-manager.properties file not found.", null);
			}
			properties.load(input);
		} catch (IOException ex) {
			throw new CacheManagerException("Failed to load cache-manager.properties.", ex);
		}
	}

	public static String getPropertyValue(String key) {
		return properties.getProperty(key);
	}

}