package br.com.ronaldomatias.cachemanager.aspect.joinpointprocessor;

import br.com.ronaldomatias.cachemanager.annotation.AnnotationDTO;
import org.aspectj.lang.ProceedingJoinPoint;

public class InvalidateCacheJoinPointProcessor extends BaseJoinPointProcessor {

	@Override
	public Object run(AnnotationDTO annotationDTO, ProceedingJoinPoint proceedingJoinPoint, Class<?> returnType) throws Throwable {
		// Por padrao, somente invalida cache se o metodo foi processado com sucesso.
		// Usuario pode optar por invalidar em casos de erro tambem.
		// TODO: Essa logica de exception pode ir para o afterThowing ao inves de ficar no around.
		try {
			Object proceedResult = proceedingJoinPoint.proceed();
			super.getRedisHandler().del(annotationDTO.getKey());
			return proceedResult;
		} catch (Throwable ex) {
			if (annotationDTO.getInvalidateOnError()) {
				super.getRedisHandler().del(annotationDTO.getKey());
			}
			throw ex;
		}
	}

}
