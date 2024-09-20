package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.util.ReflectionUtil;
import br.com.ronaldomatias.cachemanager.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CacheKeyComposer {

	public static String compose(Annotation annotation, MethodSignature methodSignature, ProceedingJoinPoint proceedingJoinPoint) {
		// TODO: Definir um limite para o tamanho de caracteres da chave.
		// TODO: Usuario pode definir um separador de parametros. Atualmente eh o underline.

		// Se key eh null ou vazio, retorna erro.
		Object key = ReflectionUtil.getAnnotationFieldValue("key", annotation);
		if (StringUtils.isEmpty(key)) {
			throw new CacheManagerException("Não foi encontrada nenhuma chave associada à anotação: " + annotation.annotationType(), null);
		}

		// Pega os valores dos parametros selecionados e monta a key.
		List<String> methodParams = Arrays.stream(methodSignature.getParameterNames()).toList();
		List<String> keyParams = Arrays.stream(key.toString().split(";")).map(String::trim).toList();
		String result = keyParams.stream()
				.map(param -> {
					if (param.startsWith("#")) {
						// Retorna parametro dinamico.
						param = param.substring(1).trim();

						if (isSingleParam(param)) {
							int indexOfParam = methodParams.indexOf(param);
							return indexOfParam == -1 ? "" : proceedingJoinPoint.getArgs()[indexOfParam].toString();
						}

						int indexOfParam = methodParams.indexOf(param.split("\\.")[0]);
						if (indexOfParam == -1) {
							return "";
						}

						return ReflectionUtil.getParamValueAsString(proceedingJoinPoint.getArgs()[indexOfParam], param.split("\\."));
					} else {
						// Retorna parametro estatico.
						return param;
					}
				})
				.filter(p -> !StringUtils.isEmpty(p))
				.map(Object::toString)
				.collect(Collectors.joining("_"));

		if (StringUtils.isEmpty(result)) {
			throw new CacheManagerException("Não foi encontrada nenhuma chave associada à anotação: " + annotation.annotationType(), null);
		}

		return result;
	}

	private static Boolean isSingleParam(String param) {
		// SingleParam -> parametro que nao necessita de acesso a algum atributo do Objeto.
		return param.split("\\.").length == 1;
	}

}
