package br.com.ronaldomatias.cachemanager.redis;

import br.com.ronaldomatias.cachemanager.redis.client.RedisClientSingleton;
import br.com.ronaldomatias.cachemanager.util.ObjectMapperUtil;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

public class RedisOperations {
	// TODO: Remover sneaky e retornar feedback na exception.
	private final Jedis client;

	public RedisOperations() {
		this.client = RedisClientSingleton.getInstance();
	}

	@SneakyThrows
	public void set(String key, Object value, Long ttl) {
		client.set(key, ObjectMapperUtil.getMapper().writeValueAsString(value));
		client.expire(key, ttl);
		client.close();
	}

	@SneakyThrows
	public Object get(String key, Class<?> returnType) {
		Object value = ObjectMapperUtil.getMapper().readValue(client.get(key), returnType);
		client.close();

		return value;
	}

	public void del(String key) {
		client.del(key);
		client.close();
	}

	public boolean existsKey(String key) {
		boolean exists = client.exists(key);
		client.close();

		return exists;
	}

}