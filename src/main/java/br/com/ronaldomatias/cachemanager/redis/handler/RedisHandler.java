package br.com.ronaldomatias.cachemanager.redis.handler;

import br.com.ronaldomatias.cachemanager.redis.manager.RedisCacheManager;
import br.com.ronaldomatias.cachemanager.redis.client.RedisClientSingleton;
import br.com.ronaldomatias.cachemanager.util.ObjectMapperUtils;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

public class RedisHandler {
	// TODO: Remover sneaky e retornar feedback na exception.
	// TODO: Adicionar um identificador mais especifico para o Set 'lastRecentUsedKeys'.
	private final Jedis redisClient;

	private final RedisCacheManager redisCacheManager;

	public RedisHandler() {
		this.redisClient = RedisClientSingleton.getInstance();
		this.redisCacheManager = new RedisCacheManager(redisClient);
	}

	@SneakyThrows
	public void set(String key, Object value, Long ttl) {
		redisCacheManager.executeCleaningStrategy(value);

		redisClient.set(key, ObjectMapperUtils.getMapper().writeValueAsString(value));
		redisClient.expire(key, ttl);
		redisClient.close();

		// Adiciona a key no Sorted Set 'lastRecentUsedKeys', que é ordenado com base no timeStamp.
		// A key mais recente fica em ultima posicao do Sorted Set.
		redisClient.zadd("lastRecentUsedKeys", System.currentTimeMillis(), key);
	}

	@SneakyThrows
	public Object get(String key, Class<?> returnType) {
		Object value = ObjectMapperUtils.getMapper().readValue(redisClient.get(key), returnType);
		redisClient.close();

		// Adiciona a key no Sorted Set 'lastRecentUsedKeys', que é ordenado com base no timeStamp.
		// A key mais recente fica em ultima posicao do Sorted Set.
		redisClient.zadd("lastRecentUsedKeys", System.currentTimeMillis(), key);

		return value;
	}

	public void del(String key) {
		redisClient.del(key);
		redisClient.close();

		// Remove a key do Sorted Set 'lastRecentUsedKeys'.
		redisClient.zrem("lastRecentUsedKeys", key);
	}

	public boolean existsKey(String key) {
		boolean exists = redisClient.exists(key);
		redisClient.close();

		return exists;
	}

}