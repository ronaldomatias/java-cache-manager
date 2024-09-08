package br.com.ronaldomatias.cachemanager.cachemanager.manipulator.component;

import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class InvalidateManipulator extends BaseManipulator {

	@Override
	public Object run(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		super.getRedisOperations().del(redisDTO.getKey());
		return proceedingJoinPoint.proceed();
	}

}
