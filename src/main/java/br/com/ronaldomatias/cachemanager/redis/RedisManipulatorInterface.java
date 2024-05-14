package br.com.ronaldomatias.cachemanager.redis;

public interface RedisManipulatorInterface {
	void set(RedisDTO value);

	String get(String key);

	boolean existsKey(String key);
}