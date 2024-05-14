package br.com.ronaldomatias.cachemanager.cachemanager.manipulators;

import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class CacheableManipulator extends BaseManipulator {

	public Object cacheable(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object response;

		if (super.getRedisOperations().existsKey(redisDTO.getKey())) {
			response = super.getRedisOperations().get(redisDTO.getKey());
		} else {
			response = proceedingJoinPoint.proceed();
			redisDTO.setValue(response);
			super.getRedisOperations().set(redisDTO);
		}

		return response;
	}

}
