package br.com.ronaldomatias.cachemanager.cachemanager.manipulator;

import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisDTO;
import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisOperations;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;

@Data
public abstract class BaseManipulator {

	private final RedisOperations redisOperations;

	public BaseManipulator() {
		this.redisOperations = new RedisOperations();
	}
	
	protected abstract Object run(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable;

}