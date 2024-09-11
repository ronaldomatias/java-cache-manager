package br.com.ronaldomatias.cachemanager.manipulator;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManipulatorFactory {

	private final static Map<Class<? extends Annotation>, BaseManipulator> components;

	public Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<? extends Annotation> annotation, Class<?> returnType) throws Throwable {
		BaseManipulator manipulator = components.get(annotation);

		if (manipulator == null) throw new CacheManagerException("No manipulator found for annotation: " + annotation, null);

		return manipulator.run(annotationDTO, proceedingJoinPoint, returnType);
	}

	static {
		components = new ConcurrentHashMap<>();
		components.put(Cacheable.class, new CacheableManipulator());
		components.put(InvalidateCache.class, new InvalidateManipulator());
	}

}
