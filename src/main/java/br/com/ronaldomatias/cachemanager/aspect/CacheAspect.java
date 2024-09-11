package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor.JoinPointProcessorFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

@org.aspectj.lang.annotation.Aspect
public class CacheAspect {
	private final JoinPointProcessorFactory joinPointProcessorFactory;

	public CacheAspect() {
		joinPointProcessorFactory = new JoinPointProcessorFactory();
	}

	@Around("@annotation(br.com.ronaldomatias.cachemanager.annotation.Cacheable)")
	public Object pointCutCacheable(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		Optional<Annotation> annotationOpt = Arrays.stream(methodSignature.getMethod().getAnnotations()).filter(a -> a.annotationType().equals(Cacheable.class)).findFirst();
		if (annotationOpt.isEmpty()) {
			throw new CacheManagerException("Method intercepted by Cacheable pointCut but not present on method scope.", null);
		}

		return joinPointProcessorFactory.run(
				annotationOpt.get(),
				proceedingJoinPoint,
				Cacheable.class,
				methodSignature.getReturnType());
	}

	@Around("@annotation(br.com.ronaldomatias.cachemanager.annotation.InvalidateCache)")
	public Object pointCutInvalidateCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		Optional<Annotation> annotation = Arrays.stream(methodSignature.getMethod().getAnnotations()).filter(a -> a.annotationType().equals(InvalidateCache.class)).findFirst();
		if (annotation.isEmpty()) {
			throw new CacheManagerException("Method intercepted by invalidateCache pointCut but not present on method scope.", null);
		}

		return joinPointProcessorFactory.run(
				annotation.get(),
				proceedingJoinPoint,
				InvalidateCache.class,
				methodSignature.getReturnType());
	}

}