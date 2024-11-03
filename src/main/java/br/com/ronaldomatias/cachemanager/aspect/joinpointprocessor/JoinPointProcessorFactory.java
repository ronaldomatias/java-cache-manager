package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import br.com.ronaldomatias.cachemanager.annotation.dto.AnnotationFieldsValueDTO;
import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.redis.config.key.RedisKeyComposer;
import br.com.ronaldomatias.cachemanager.util.ReflectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JoinPointProcessorFactory {

	private final static Map<Class<? extends Annotation>, BaseJoinPointProcessor> processors;

	static {
		processors = new ConcurrentHashMap<>();
		processors.put(Cacheable.class, new CacheableJoinPointProcessor());
		processors.put(InvalidateCache.class, new InvalidateCacheJoinPointProcessor());
	}

	public Object run(Annotation annotation, ProceedingJoinPoint proceedingJoinPoint, MethodSignature methodSignature, Class<? extends Annotation> processorKey, Class<?> returnType) throws Throwable {
		BaseJoinPointProcessor joinPointProcessor = processors.get(processorKey);
		if (joinPointProcessor == null) {
			throw new CacheManagerException("JoinPointProcessor é inexistente: %s.".formatted(processorKey));
		}

		return joinPointProcessor.run(
				new AnnotationFieldsValueDTO(
						RedisKeyComposer.compose(annotation, methodSignature, proceedingJoinPoint),
						(Long) ReflectionUtils.getAnnotationFieldValue("ttl", annotation),
						(Boolean) ReflectionUtils.getAnnotationFieldValue("invalidateOnError", annotation)
				),
				proceedingJoinPoint,
				returnType);
	}

}
