package br.com.ronaldomatias.cachemanager.cachemanager;

import br.com.ronaldomatias.cachemanager.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.redis.RedisManipulator;
import br.com.ronaldomatias.cachemanager.redis.RedisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.util.List;

public class CacheManager {
	public static final List<Class<? extends Annotation>> allAnnotations = List.of(Cacheable.class);

	private final RedisManipulator redisManipulator;

	public CacheManager() {
		this.redisManipulator = new RedisManipulator();
	}

	public Object run(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint, Class<? extends Annotation> aClass, Class<?> returnType) throws Throwable {
		// TODO: Aplicar o padrão Factory.
		if (aClass.equals(Cacheable.class)) {
			Object cacheable = this.cacheable(redisDTO, proceedingJoinPoint);
			return new ObjectMapper().convertValue(cacheable, returnType);
		}

		throw new RuntimeException();
	}

	private Object cacheable(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object response;

		if (redisManipulator.existsKey(redisDTO.getKey())) {
			response = redisManipulator.get(redisDTO.getKey());
		} else {
			response = proceedingJoinPoint.proceed();
			redisDTO.setValue(response);
			redisManipulator.set(redisDTO);
		}

		return response;
	}


}