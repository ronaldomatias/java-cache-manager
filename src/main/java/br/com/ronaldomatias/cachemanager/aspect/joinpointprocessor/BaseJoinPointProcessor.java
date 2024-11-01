package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.redis.handler.RedisHandler;
import br.com.ronaldomatias.cachemanager.util.ReflectionUtils;
import br.com.ronaldomatias.cachemanager.util.StringUtils;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public abstract class BaseJoinPointProcessor {

	private final RedisHandler redisHandler;

	public BaseJoinPointProcessor() {
		this.redisHandler = new RedisHandler();
	}
	
	protected abstract Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable;

	protected String composeKey(Annotation annotation, MethodSignature methodSignature, ProceedingJoinPoint proceedingJoinPoint) {
		// Se key eh null ou vazio, retorna erro.
		Object key = ReflectionUtils.getAnnotationFieldValue("key", annotation);
		if (StringUtils.isEmpty(key)) {
			throw new CacheManagerException("Não foi encontrada nenhuma chave associada à anotação: " + annotation.annotationType());
		}

		// Pega os valores dos parametros do metodo e monta a key.
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

						return ReflectionUtils.getParamValueAsString(proceedingJoinPoint.getArgs()[indexOfParam], param.split("\\."));
					} else {
						// Retorna parametro estatico.
						return param;
					}
				})
				.filter(p -> !StringUtils.isEmpty(p))
				.map(Object::toString)
				.collect(Collectors.joining("_"));

		if (StringUtils.isEmpty(result)) {
			throw new CacheManagerException("Não foi encontrada nenhuma chave associada à anotação: " + annotation.annotationType());
		}

		return result;
	}

	private Boolean isSingleParam(String param) {
		// SingleParam -> Parametro do metodo que nao necessita de acesso a algum atributo do Objeto.
		return param.split("\\.").length == 1;
	}

}