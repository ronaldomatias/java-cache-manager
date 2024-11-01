package br.com.ronaldomatias.cachemanager.redis.handler;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.redis.client.RedisConnectionFactory;
import br.com.ronaldomatias.cachemanager.util.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import redis.clients.jedis.Jedis;

public class RedisHandler {

	public void set(String key, Object value, Long ttl) {
		try (Jedis redisClient = RedisConnectionFactory.getInstance().getConnection()) {
			redisClient.set(key, ObjectMapperUtils.getMapper().writeValueAsString(value));
			redisClient.expire(key, ttl);
		} catch (JsonProcessingException e) {
			throw new CacheManagerException("Erro de operação: Set");
		}
	}

	public Object get(String key, Class<?> returnType) {
		try (Jedis redisClient = RedisConnectionFactory.getInstance().getConnection()) {
			return ObjectMapperUtils.getMapper().readValue(redisClient.get(key), returnType);
		} catch (JsonProcessingException e) {
			throw new CacheManagerException("Erro de operação: Get");
		}
	}

	public void del(String key) {
		try (Jedis redisClient = RedisConnectionFactory.getInstance().getConnection()) {
			redisClient.del(key);
		}
	}

	public boolean existsKey(String key) {
		try (Jedis redisClient = RedisConnectionFactory.getInstance().getConnection()) {
			return redisClient.exists(key);
		}
	}

}
