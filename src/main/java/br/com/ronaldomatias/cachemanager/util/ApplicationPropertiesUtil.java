package br.com.ronaldomatias.cachemanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesUtil {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ApplicationPropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                // TODO: Retornar feedback ao usuario na exception;
                System.out.println("Unable to find application.properties.");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}