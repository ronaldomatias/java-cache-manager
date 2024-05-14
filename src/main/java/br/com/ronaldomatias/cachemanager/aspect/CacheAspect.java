package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.cachemanager.CacheManager;
import br.com.ronaldomatias.cachemanager.redis.RedisDTO;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;

@Log4j2
@org.aspectj.lang.annotation.Aspect
public class CacheAspect {
	private final CacheManager cacheManager;

	public CacheAspect() {
		this.cacheManager = new CacheManager();
	}

	@Around("execution(* br.com.ronaldomatias.cachemanager.tester.service..*(..)))") // TODO: Como configurar isso dinamicamente ?
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		Annotation annotation = Arrays.stream(methodSignature.getMethod().getAnnotations()).filter(a -> CacheManager.allAnnotations.contains(a.annotationType())).findFirst().orElse(null);
		if (annotation == null) {
			return proceedingJoinPoint.proceed();
		}

		return cacheManager.run(new RedisDTO(annotation), proceedingJoinPoint, annotation.annotationType(), methodSignature.getReturnType());
	}

}