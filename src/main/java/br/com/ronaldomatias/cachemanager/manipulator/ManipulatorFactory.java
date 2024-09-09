package br.com.ronaldomatias.cachemanager.manipulator;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import br.com.ronaldomatias.cachemanager.redis.RedisDTO;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManipulatorFactory {

	private final static Map<Class<? extends Annotation>, BaseManipulator> components;

	public Object run(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint, Class<? extends Annotation> annotation, Class<?> returnType) throws Throwable {
		BaseManipulator manipulator = components.get(annotation);

		if (manipulator == null) throw new IllegalArgumentException("No manipulator found for annotation: " + annotation);

		return manipulator.run(redisDTO, proceedingJoinPoint, returnType);
	}

	static {
		components = new ConcurrentHashMap<>();
		components.put(Cacheable.class, new CacheableManipulator());
		components.put(InvalidateCache.class, new InvalidateManipulator());
	}

}
