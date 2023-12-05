package br.com.ronaldomatias.cachemanager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CacheAspect {

	@Pointcut("@annotation(br.com.ronaldomatias.cachemanager.annotation.Cacheable) && execution(* *(..))")
	public void cacheablePointCut() {
	}

	@Around("cacheablePointCut()")
	public Object cacheableJoinPoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.printf("Interceptado.");
		return proceedingJoinPoint.proceed();
	}

}
