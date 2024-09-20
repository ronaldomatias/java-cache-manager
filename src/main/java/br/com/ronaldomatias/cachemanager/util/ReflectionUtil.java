package br.com.ronaldomatias.cachemanager.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

	public static Object getAnnotationFieldValue(String methodName, Object object) {
		try {
			Method getMethod = object.getClass().getMethod(methodName);
			return getMethod.invoke(object);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			// TODO: Retornar feedback na exception.
			return null;
		}
	}

	public static Object getParamValueAsString(Object obj, String[] params) {
		if (obj == null || params == null || params.length == 0) return null;

		Object result = obj;

		for (int i = 1; i < params.length; i++) {
			try {
				Field field = result.getClass().getDeclaredField(params[i]);
				field.setAccessible(true);
				result = field.get(result);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				// TODO: Retornar feedback aqui.
			}

			if (result == null) {
				return "";
			}
		}

		return result.toString();
	}

}
