package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor.JoinPointProcessorFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@org.aspectj.lang.annotation.Aspect
public class CacheAspect {
	private final JoinPointProcessorFactory joinPointProcessorFactory;

	public CacheAspect() {
		joinPointProcessorFactory = new JoinPointProcessorFactory();
	}

	@Around("@annotation(br.com.ronaldomatias.cachemanager.annotation.Cacheable)")
	public Object pointCutCacheable(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		Annotation annotation = Arrays.stream(methodSignature.getMethod().getAnnotations()).filter(a -> a.annotationType().equals(Cacheable.class)).findFirst().get();

		return joinPointProcessorFactory.run(
				annotation,
				proceedingJoinPoint,
				methodSignature,
				Cacheable.class,
				methodSignature.getReturnType());
	}

	@Around("@annotation(br.com.ronaldomatias.cachemanager.annotation.InvalidateCache)")
	public Object pointCutInvalidateCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		Annotation annotation = Arrays.stream(methodSignature.getMethod().getAnnotations()).filter(a -> a.annotationType().equals(InvalidateCache.class)).findFirst().get();

		return joinPointProcessorFactory.run(
				annotation,
				proceedingJoinPoint,
				methodSignature,
				InvalidateCache.class,
				methodSignature.getReturnType());
	}

}