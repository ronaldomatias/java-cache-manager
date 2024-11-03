package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.dto.AnnotationFieldsValueDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class InvalidateCacheJoinPointProcessor extends BaseJoinPointProcessor {

	@Override
	public Object run(AnnotationFieldsValueDTO annotationFieldsValueDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		// Por padrao, somente invalida cache se o metodo foi processado com sucesso.
		// Usuario pode optar por invalidar em casos de erro tambem.
		// TODO: Essa logica de exception pode ir para o afterThowing ao inves de ficar no around.
		try {
			Object proceedResult = proceedingJoinPoint.proceed();
			super.getRedisHandler().del(annotationFieldsValueDTO.getKey());
			return proceedResult;
		} catch (Throwable ex) {
			if (annotationFieldsValueDTO.getInvalidateOnError()) {
				super.getRedisHandler().del(annotationFieldsValueDTO.getKey());
			}
			throw ex;
		}
	}

}
