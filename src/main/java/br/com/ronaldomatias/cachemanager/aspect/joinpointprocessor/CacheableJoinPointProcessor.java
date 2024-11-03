package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.dto.AnnotationFieldsValueDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class CacheableJoinPointProcessor extends BaseJoinPointProcessor {

	@Override
	public Object run(AnnotationFieldsValueDTO annotationFieldsValueDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		Object resultOfJoinPointProceeding;

		if (super.getRedisHandler().existsKey(annotationFieldsValueDTO.getKey())) {
			resultOfJoinPointProceeding = super.getRedisHandler().get(annotationFieldsValueDTO.getKey(), returnType);

		} else {
			resultOfJoinPointProceeding = proceedingJoinPoint.proceed();
			super.getRedisHandler().set(annotationFieldsValueDTO.getKey(), resultOfJoinPointProceeding, annotationFieldsValueDTO.getTtl());
		}

		return resultOfJoinPointProceeding;
	}

}
