package br.com.ronaldomatias.cachemanager.util;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

	public static Object getAnnotationFieldValue(String methodName, Object obj) throws CacheManagerException {
		try {
			Method getMethod = obj.getClass().getMethod(methodName);
			return getMethod.invoke(obj);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new CacheManagerException(
					"Não foi possível obter o valor do método: %s da anotação: %s.".formatted(
							methodName,
							obj.getClass().getSimpleName()),
					e.getCause()
			);
		}
	}

	public static Object getParamValueAsString(Object obj, String[] params) throws CacheManagerException {
		if (obj == null) {
			return "";
		} else if (params.length == 1) {
			return obj.toString(); // TODO: Limitar tamanho aqui.
		}

		Object result = obj;

		for (int i = 1; i < params.length; i++) {
			try {
				Field field = result.getClass().getDeclaredField(params[i]);
				field.setAccessible(true);
				result = field.get(result);
			} catch (NoSuchFieldException e) {
				throw new CacheManagerException(
						"Não foi encontrado o parâmetro: %s, do Objeto: %s, para compor a chave-dinâmica.".formatted(
								params[i],
								obj.getClass().getSimpleName())
				);
			} catch (IllegalAccessException e) {
				throw new CacheManagerException(
						"Não foi possível acessar o parâmetro: %s, do Objeto: %s, para compor a chave-dinâmica.".formatted(
								params[i],
								obj.getClass().getSimpleName())
				);
			}
		}

		return result.toString();
	}

}
