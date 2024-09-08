package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.cachemanager.manipulator.ManipulatorFactory;
import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisDTO;
import br.com.ronaldomatias.cachemanager.util.ReflectionUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

@Log4j2
@org.aspectj.lang.annotation.Aspect
public class CacheAspect {
	private final ManipulatorFactory manipulatorFactory;

	public CacheAspect() {
		manipulatorFactory = new ManipulatorFactory();
	}

	@Around("@annotation(br.com.ronaldomatias.cachemanager.annotation.Cacheable) || @annotation(br.com.ronaldomatias.cachemanager.annotation.InvalidateCache)")
	public Object pointCut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		Optional<Annotation> annotation = Arrays.stream(methodSignature.getMethod().getAnnotations()).filter(a -> ReflectionUtil.getAnnotationValue("key", a) != null).findFirst();
		if (annotation.isEmpty()) {
			return proceedingJoinPoint.proceed();
		}

		return manipulatorFactory.run(new RedisDTO(annotation.get()), proceedingJoinPoint, annotation.get().annotationType(), methodSignature.getReturnType());
	}

}