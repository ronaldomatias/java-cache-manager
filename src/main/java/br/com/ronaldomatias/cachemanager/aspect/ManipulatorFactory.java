package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.cachemanager.manipulators.CacheableManipulator;
import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisDTO;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;

public class ManipulatorFactory {

	private final CacheableManipulator cacheableManipulator;

	public ManipulatorFactory() {
		this.cacheableManipulator = new CacheableManipulator();
	}

	public Object run(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint, Class<? extends Annotation> annotation, Class returnType) throws Throwable {
		// TODO: A desserializacao deve ser feita por aqui
		if (annotation.equals(Cacheable.class)) {
			return cacheableManipulator.cacheable(redisDTO, proceedingJoinPoint);
		}

		throw new RuntimeException();
	}

}
