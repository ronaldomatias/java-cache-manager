package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class CacheableJoinPointProcessor extends BaseJoinPointProcessor {

	@Override
	public Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		Object proceedResult;

		if (super.getRedisOperations().existsKey(annotationDTO.getKey())) {
			proceedResult = super.getRedisOperations().get(annotationDTO.getKey(), returnType);
		} else {
			proceedResult = proceedingJoinPoint.proceed();
			// TODO: Validar memoria disponivel antes de adicionar.
			super.getRedisOperations().set(annotationDTO.getKey(), proceedResult, annotationDTO.getTtl());
		}

		return proceedResult;
	}

}
