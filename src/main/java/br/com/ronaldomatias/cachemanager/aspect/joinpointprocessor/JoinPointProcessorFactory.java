package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import br.com.ronaldomatias.cachemanager.util.ReflectionUtil;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JoinPointProcessorFactory {

	private final static Map<Class<? extends Annotation>, BaseJoinPointProcessor> components;

	public Object run(Annotation annotation, ProceedingJoinPoint proceedingJoinPoint, Class<? extends Annotation> componentKey, Class<?> returnType) throws Throwable {
		BaseJoinPointProcessor joinPointProcessor = components.get(componentKey);

		if (joinPointProcessor == null) throw new CacheManagerException("No manipulator found for annotation: " + componentKey, null);

		return joinPointProcessor.run(this.createAnnotationDTO(annotation), proceedingJoinPoint, returnType);
	}

	static {
		components = new ConcurrentHashMap<>();
		components.put(Cacheable.class, new CacheableJoinPointProcessor());
		components.put(InvalidateCache.class, new InvalidateCacheJoinPointProcessor());
	}

	private AnnotationDTO createAnnotationDTO(Annotation annotation) {
		return new AnnotationDTO(
				(String) ReflectionUtil.getAnnotationValue("key", annotation),
				(Long) ReflectionUtil.getAnnotationValue("ttl", annotation),
				(Boolean) ReflectionUtil.getAnnotationValue("invalidateOnError", annotation)
		);
	}

}
