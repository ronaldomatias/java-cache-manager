package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.util.ReflectionUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;


@org.aspectj.lang.annotation.Aspect
@Log4j2
@Component
public class CacheAspect {
	CacheManagerService cacheManagerService;

	public CacheAspect(CacheManagerService cacheManagerService) {
		this.cacheManagerService = cacheManagerService;
	}

	@Around("execution(* br.com.ronaldomatias.cachemanager.tester.service..*(..)))") // TODO: Como configurar isso dinamicamente ?
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		Annotation annotation = Arrays.stream(methodSignature.getMethod().getAnnotations()).filter(a -> CacheManagerService.allAnnotations.contains(a.annotationType())).findFirst().orElse(null);
		if (annotation == null) {
			return proceedingJoinPoint.proceed();
		}

		return cacheManagerService.run(new RedisDTO(annotation), proceedingJoinPoint, annotation.annotationType(), methodSignature.getReturnType());
	}

}