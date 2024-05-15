package br.com.ronaldomatias.cachemanager.cachemanager.redis;

import br.com.ronaldomatias.cachemanager.util.ReflectionUtil;
import lombok.Data;

import java.lang.annotation.Annotation;

@Data
public class RedisDTO {
	private String key;
	private Long ttl;

	public RedisDTO(Annotation annotation) {
		this.key = (String) ReflectionUtil.getAnnotationValue("key", annotation);
		this.ttl = (Long) ReflectionUtil.getAnnotationValue("ttl", annotation);
	}

}