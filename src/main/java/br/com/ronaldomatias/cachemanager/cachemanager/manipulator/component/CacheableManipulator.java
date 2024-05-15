package br.com.ronaldomatias.cachemanager.cachemanager.manipulator.component;

import br.com.ronaldomatias.cachemanager.cachemanager.manipulator.BaseManipulator;
import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class CacheableManipulator extends BaseManipulator {

	@Override
	public Object run(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		Object response;

		if (super.getRedisOperations().existsKey(redisDTO.getKey())) {
			response = super.getRedisOperations().get(redisDTO.getKey(), returnType);
		} else {
			response = proceedingJoinPoint.proceed();
			super.getRedisOperations().set(redisDTO.getKey(), response, redisDTO.getTtl());
		}

		return response;
	}

}
