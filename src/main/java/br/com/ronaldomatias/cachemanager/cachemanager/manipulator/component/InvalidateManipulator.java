package br.com.ronaldomatias.cachemanager.cachemanager.manipulator.component;

import br.com.ronaldomatias.cachemanager.cachemanager.manipulator.BaseManipulator;
import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class InvalidateManipulator extends BaseManipulator {

	@Override
	public Object run(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		return proceedingJoinPoint.proceed();
	}

}
