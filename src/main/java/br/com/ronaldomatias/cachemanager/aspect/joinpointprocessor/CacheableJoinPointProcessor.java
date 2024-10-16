package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Map;

public class CacheableJoinPointProcessor extends BaseJoinPointProcessor {

	@Override
	public Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		Object resultOfJoinPointProceeding;

		if (super.getRedisOperations().existsKey(annotationDTO.getKey())) {
			resultOfJoinPointProceeding = super.getRedisOperations().get(annotationDTO.getKey(), returnType);

		} else {
			resultOfJoinPointProceeding = proceedingJoinPoint.proceed();
			super.getRedisOperations().set(annotationDTO.getKey(), resultOfJoinPointProceeding, annotationDTO.getTtl());
		}

		return resultOfJoinPointProceeding;
	}

}
