package br.com.ronaldomatias.cachemanager.manipulator;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class InvalidateManipulator extends BaseManipulator {

	@Override
	public Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		super.getRedisOperations().del(annotationDTO.getKey());
		return proceedingJoinPoint.proceed();
	}

}
