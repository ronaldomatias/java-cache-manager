package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.util.ReflectionUtil;
import br.com.ronaldomatias.cachemanager.util.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CacheKeyComposer {

	public static String compose(Annotation annotation, MethodSignature methodSignature, ProceedingJoinPoint proceedingJoinPoint) {
		// TODO: Definir um limite para o tamanho de caracteres da chave.
		// Se possui key estatica, a retorna.
		Object key = ReflectionUtil.getAnnotationFieldValue("key", annotation);
		if (!StringUtils.isEmpty(key)) {
			return key.toString();
		}

		return composeDynamicKey(annotation, methodSignature, proceedingJoinPoint);
	}

	private static String composeDynamicKey(Annotation annotation, MethodSignature methodSignature, ProceedingJoinPoint proceedingJoinPoint) {
		// TODO: Usuario pode definir um separador de parametros. Atualmente eh o underline.
		// Se nao tem key fixa e nem dinamica, retorna erro.
		Object dynamicKey = ReflectionUtil.getAnnotationFieldValue("dynamicKey", annotation);
		if (StringUtils.isEmpty(dynamicKey)) {
			throw new CacheManagerException("Não foi encontrada nenhuma chave associada à anotação: " + annotation.annotationType(), null);
		}

		// Pega os valores dos parametros selecionados e monta a key.
		List<String> methodParams = Arrays.stream(methodSignature.getParameterNames()).toList();
		List<String> dynamicKeyParams = Arrays.stream(dynamicKey.toString().split(";")).map(String::trim).toList();
		String key = dynamicKeyParams.stream()
				.map(param -> {
					if (param.startsWith("#")) {
						// Retorna parametro dinamico.
						String nameFromDynamicParam = param.substring(1).trim();
						int indexFromDynamicParam = methodParams.indexOf(nameFromDynamicParam);

						return indexFromDynamicParam != -1 ? Objects.toString(proceedingJoinPoint.getArgs()[indexFromDynamicParam], "") : "";
					} else {
						// Retorna parametro estatico.
						return param;
					}
				}).collect(Collectors.joining("_"));

		if (StringUtils.isEmpty(key)) {
			throw new CacheManagerException("Não foi encontrada nenhuma chave associada à anotação: " + annotation.annotationType(), null);
		}

		return key;
	}

}
