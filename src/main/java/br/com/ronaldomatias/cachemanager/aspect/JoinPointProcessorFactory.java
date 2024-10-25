package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor.BaseJoinPointProcessor;
import br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor.CacheableJoinPointProcessor;
import br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor.InvalidateCacheJoinPointProcessor;
import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
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

	public Object run(Annotation annotation, ProceedingJoinPoint proceedingJoinPoint, MethodSignature methodSignature, Class<? extends Annotation> componentKey, Class<?> returnType) throws Throwable {
		BaseJoinPointProcessor joinPointProcessor = processors.get(componentKey);
		if (joinPointProcessor == null) {
			throw new CacheManagerException("No manipulator found for annotation: " + componentKey, null);
		}

		return joinPointProcessor.run(this.createAnnotationDTO(annotation, proceedingJoinPoint, methodSignature), proceedingJoinPoint, returnType);
	}

	private AnnotationDTO createAnnotationDTO(Annotation annotation, ProceedingJoinPoint proceedingJoinPoint, MethodSignature methodSignature) {
		return new AnnotationDTO(
				CacheKeyComposer.compose(annotation, methodSignature, proceedingJoinPoint),
				(Long) ReflectionUtils.getAnnotationFieldValue("ttl", annotation),
				(Boolean) ReflectionUtils.getAnnotationFieldValue("invalidateOnError", annotation)
		);
	}

}
