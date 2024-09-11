package br.com.ronaldomatias.cachemanager.manipulator;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class CacheableManipulator extends BaseManipulator {

	@Override
	public Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		Object response;

		if (super.getRedisOperations().existsKey(annotationDTO.getKey())) {
			response = super.getRedisOperations().get(annotationDTO.getKey(), returnType);
		} else {
			response = proceedingJoinPoint.proceed();
			// verificar memoria antes de adicionar novo.
			super.getRedisOperations().set(annotationDTO.getKey(), response, annotationDTO.getTtl());
		}

		return response;
	}

}
