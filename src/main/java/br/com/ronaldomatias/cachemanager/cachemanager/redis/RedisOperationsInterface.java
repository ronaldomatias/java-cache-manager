package br.com.ronaldomatias.cachemanager.cachemanager.redis;

public interface RedisOperationsInterface {
	void set(RedisDTO value);

	String get(String key);

	boolean existsKey(String key);
}