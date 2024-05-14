package br.com.ronaldomatias.cachemanager.cachemanager.redis;

public interface RedisOperationsInterface {
	void set(String key, Object value, Long ttl);

	String get(String key);

	boolean existsKey(String key);
}