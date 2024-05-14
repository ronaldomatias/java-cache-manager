package br.com.ronaldomatias.cachemanager.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

	public static Object getAnnotationValue(String methodName, Object object) {
		try {
			Method getMethod = object.getClass().getMethod(methodName);
			return getMethod.invoke(object);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
