package br.com.ronaldomatias.cachemanager.cachemanager.manipulator;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import br.com.ronaldomatias.cachemanager.cachemanager.manipulator.component.CacheableManipulator;
import br.com.ronaldomatias.cachemanager.cachemanager.manipulator.component.InvalidateManipulator;
import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ManipulatorFactory {

	private static final Map<Class<? extends Annotation>, BaseManipulator> components;

	public Object runManipulator(RedisDTO redisDTO, ProceedingJoinPoint proceedingJoinPoint, Class<? extends Annotation> annotation, Class returnType) throws Throwable {
		// TODO: A desserializacao deve ser feita por aqui

		Object run = components.get(annotation).run(redisDTO, proceedingJoinPoint);
		run = new ObjectMapper().convertValue(run, returnType);
		return run;
	}

	static {
		components = new HashMap<>();
		components.put(Cacheable.class, new CacheableManipulator());
		components.put(InvalidateCache.class, new InvalidateManipulator());
	}

}
