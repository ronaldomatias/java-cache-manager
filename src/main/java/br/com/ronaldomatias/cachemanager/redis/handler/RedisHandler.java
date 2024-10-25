package br.com.ronaldomatias.cachemanager.redis.handler;

import br.com.ronaldomatias.cachemanager.redis.client.RedisClientInitializer;
import br.com.ronaldomatias.cachemanager.redis.manager.RedisCacheManager;
import br.com.ronaldomatias.cachemanager.util.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import redis.clients.jedis.Jedis;

public class RedisHandler {
	private final RedisCacheManager redisCacheManager;

	public RedisHandler() {
		this.redisCacheManager = new RedisCacheManager();
	}

	public void set(String key, Object value, Long ttl) {
		try (Jedis redisClient = RedisClientInitializer.getClient()) {
			redisCacheManager.executeCleaningStrategy(value, redisClient);

			redisClient.set(key, ObjectMapperUtils.getMapper().writeValueAsString(value));
			redisClient.expire(key, ttl);
			redisClient.zadd("lastRecentUsedKeys", System.currentTimeMillis(), key);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public Object get(String key, Class<?> returnType) {
		try (Jedis redisClient = RedisClientInitializer.getClient()) {
			Object value = ObjectMapperUtils.getMapper().readValue(redisClient.get(key), returnType);
			// Adiciona a key no Sorted Set 'lastRecentUsedKeys'.
			redisClient.zadd("lastRecentUsedKeys", System.currentTimeMillis(), key);
			return value;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public void del(String key) {
		try (Jedis redisClient = RedisClientInitializer.getClient()) {
			redisClient.del(key);
			// Remove a key do Sorted Set 'lastRecentUsedKeys'.
			redisClient.zrem("lastRecentUsedKeys", key);
		}
	}

	public boolean existsKey(String key) {
		try (Jedis redisClient = RedisClientInitializer.getClient()) {
			return redisClient.exists(key);
		}
	}
}
