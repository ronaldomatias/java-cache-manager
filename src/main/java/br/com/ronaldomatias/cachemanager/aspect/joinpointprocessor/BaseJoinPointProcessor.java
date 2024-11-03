package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.dto.AnnotationFieldsValueDTO;
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
	
	protected abstract Object run(AnnotationFieldsValueDTO annotationFieldsValueDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable;

}