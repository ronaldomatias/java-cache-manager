package br.com.ronaldomatias.cachemanager.cachemanager.manipulators;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.cachemanager.redis.RedisOperations;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.List;

@Data
public abstract class BaseManipulator {

	public static final List<Class<? extends Annotation>> allAnnotations = List.of(Cacheable.class);

	private final RedisOperations redisOperations;

	public BaseManipulator() {
		this.redisOperations = new RedisOperations();
	}

}