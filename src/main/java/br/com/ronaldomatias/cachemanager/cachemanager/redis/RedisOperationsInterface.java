package br.com.ronaldomatias.cachemanager.cachemanager.redis;

public interface RedisOperationsInterface {
	void set(String key, Object value, Long ttl);

	Object get(String key, Class<?> returnType);

	void del(String key);

	boolean existsKey(String key);
}