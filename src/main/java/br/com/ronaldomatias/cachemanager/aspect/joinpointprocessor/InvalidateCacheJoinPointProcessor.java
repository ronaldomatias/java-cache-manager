package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class InvalidateCacheJoinPointProcessor extends BaseJoinPointProcessor {

	@Override
	public Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		// Por padrao, somente invalida cache se o metodo foi processado com sucesso.
		// Usuario pode optar por invalidar em casos de erro tambem.
		try {
			Object proceedResult = proceedingJoinPoint.proceed();
			super.getRedisOperations().del(annotationDTO.getKey());
			return proceedResult;
		} catch (Throwable ex) {
			if (annotationDTO.getInvalidateOnError()) {
				super.getRedisOperations().del(annotationDTO.getKey());
			}
			throw ex;
		}
	}

}
