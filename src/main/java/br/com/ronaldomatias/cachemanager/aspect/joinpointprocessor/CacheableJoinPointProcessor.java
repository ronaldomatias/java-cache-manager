package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class CacheableJoinPointProcessor extends BaseJoinPointProcessor {

	@Override
	public Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		Object resultOfJoinPointProceeding;

		if (super.getRedisHandler().existsKey(annotationDTO.getKey())) {
			resultOfJoinPointProceeding = super.getRedisHandler().get(annotationDTO.getKey(), returnType);

		} else {
			resultOfJoinPointProceeding = proceedingJoinPoint.proceed();
			super.getRedisHandler().set(annotationDTO.getKey(), resultOfJoinPointProceeding, annotationDTO.getTtl());
		}

		return resultOfJoinPointProceeding;
	}

}
