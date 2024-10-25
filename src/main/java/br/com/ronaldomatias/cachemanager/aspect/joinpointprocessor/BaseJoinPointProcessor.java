package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import br.com.ronaldomatias.cachemanager.redis.handler.RedisHandler;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;

@Data
public abstract class BaseJoinPointProcessor {

	private final RedisHandler redisHandler;

	public BaseJoinPointProcessor() {
		this.redisHandler = new RedisHandler();
	}
	
	public abstract Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable;

}