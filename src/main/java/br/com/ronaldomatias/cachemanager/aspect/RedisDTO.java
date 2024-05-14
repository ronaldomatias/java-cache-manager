package br.com.ronaldomatias.cachemanager.aspect;

import br.com.ronaldomatias.cachemanager.util.ReflectionUtil;
import lombok.Data;

import java.lang.annotation.Annotation;

@Data
public class RedisDTO {
	private String key;
	private Object value;
	private Long ttl;

	public RedisDTO(Annotation annotation) {
		this.key = (String) ReflectionUtil.getAnnotationValue("key", annotation);
		this.ttl = (Long) ReflectionUtil.getAnnotationValue("ttl", annotation);
	}

}