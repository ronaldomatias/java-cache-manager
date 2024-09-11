package br.com.ronaldomatias.cachemanager.annotation;

import br.com.ronaldomatias.cachemanager.util.ReflectionUtil;
import lombok.Data;

import java.lang.annotation.Annotation;

@Data
public class AnnotationDTO {

	private String key;

	private Long ttl;

	public AnnotationDTO(Annotation annotation) {
		// TODO: E se annotation nao possuir o methodName ? avaliar se vai quebrar ou simplesmente retornar null
		this.key = (String) ReflectionUtil.getAnnotationValue("key", annotation);
		this.ttl = (Long) ReflectionUtil.getAnnotationValue("ttl", annotation);
	}

}