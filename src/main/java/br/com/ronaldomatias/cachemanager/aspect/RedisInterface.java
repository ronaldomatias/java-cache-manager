package br.com.ronaldomatias.cachemanager.aspect;

public interface RedisInterface {
	void set(RedisDTO value);

	String get(String key);

	boolean existsKey(String key);
}