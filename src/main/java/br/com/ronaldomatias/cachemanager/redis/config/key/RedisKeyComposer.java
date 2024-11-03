package br.com.ronaldomatias.cachemanager.redis.config.key;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.util.ReflectionUtils;
import br.com.ronaldomatias.cachemanager.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RedisKeyComposer {
	private static String keyPrefix;

	public static String compose(Annotation annotation, MethodSignature methodSignature, ProceedingJoinPoint proceedingJoinPoint) {
		Object key = ReflectionUtils.getAnnotationFieldValue("key", annotation);

		validateEmptyKey(key, annotation, methodSignature);

		// Pega os valores dos parametros do metodo e compoe a key.
		List<String> methodParams = Arrays.stream(methodSignature.getParameterNames()).toList();
		List<String> keyParams = Arrays.stream(key.toString().split(";")).map(String::trim).toList();

		String result = keyParams.stream()
				.map(param -> {
					if (param.startsWith("#")) {
						return composeByDynamicParam(annotation, methodSignature, proceedingJoinPoint, methodParams, param);
					}

					return composeByStaticParam(param);
				})
				.filter(p -> !StringUtils.isEmpty(p))
				.map(Object::toString)
				.collect(Collectors.joining("_"));

		validateEmptyKey(result, annotation, methodSignature);

		return keyPrefix == null ? result : keyPrefix + result;
	}

	private static Object composeByDynamicParam(Annotation annotation, MethodSignature methodSignature, ProceedingJoinPoint proceedingJoinPoint, List<String> methodParams, String param) {
		// Retorna parametro dinamico.
		param = param.substring(1).trim();
		int indexOfParam;

		if (isSingleParam(param)) {
			indexOfParam = methodParams.indexOf(param);

			validateIndexOfParam(annotation, methodSignature, param, indexOfParam);

			return proceedingJoinPoint.getArgs()[indexOfParam].toString();
		}

		indexOfParam = methodParams.indexOf(param.split("\\.")[0]);

		validateIndexOfParam(annotation, methodSignature, param, indexOfParam);

		return ReflectionUtils.getParamValueAsString(proceedingJoinPoint.getArgs()[indexOfParam], param.split("\\."));
	}

	private static String composeByStaticParam(String param) {
		// Retorna parametro estatico.
		return param;
	}

	private static void validateEmptyKey(Object key, Annotation annotation, MethodSignature methodSignature) throws CacheManagerException {
		// Se key eh null ou vazio, entao retorna erro.
		if (StringUtils.isEmpty(key)) {
			throw new CacheManagerException(
					"Não foi encontrada nenhuma chave associada à anotação: %s definida no método: %s.".formatted(
							annotation.annotationType(),
							methodSignature.getMethod().getName())
			);
		}
	}

	private static void validateIndexOfParam(Annotation annotation, MethodSignature methodSignature, String param, int indexOfParam) throws CacheManagerException {
		if (indexOfParam == -1) {
			throw new CacheManagerException(
					"Não foi possível compor a chave-dinâmica, devido ao parâmetro: %s, associado à anotação: %s, definida no método: %s não existir.".formatted(
							param,
							methodSignature.getName(),
							annotation.annotationType())
			);
		}
	}

	private static Boolean isSingleParam(String param) {
		// SingleParam -> Parametro do metodo que nao necessita de acesso a algum atributo do Objeto.
		return param.split("\\.").length == 1;
	}

	public static void setKeyPrefix(String p) {
		keyPrefix = p;
	}

}
