package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;

@Service
public class CacheManagerService {
	public static final List<Class<? extends Annotation>> allAnnotations = List.of(Cacheable.class);

	private final RedisBase redisBase;

	public CacheManagerService(RedisBase redisBase) {
		this.redisBase = redisBase;
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

		if (redisBase.existsKey(redisDTO.getKey())) {
			response = redisBase.get(redisDTO.getKey());
		} else {
			response = proceedingJoinPoint.proceed();
			redisDTO.setValue(response);
			redisBase.set(redisDTO);
		}

		return response;
	}



}