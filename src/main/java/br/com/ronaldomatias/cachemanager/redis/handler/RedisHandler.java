package br.com.ronaldomatias.cachemanager.redis.handler;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import br.com.ronaldomatias.cachemanager.redis.config.client.RedisConnectionFactory;
import br.com.ronaldomatias.cachemanager.redis.monitor.StatisticsMonitor;
import br.com.ronaldomatias.cachemanager.util.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import redis.clients.jedis.Jedis;

public class RedisHandler {

	private final StatisticsMonitor statisticsMonitor;

	public RedisHandler() {
		this.statisticsMonitor = new StatisticsMonitor();
	}

	public void set(String key, Object value, Long ttl) {
		try (Jedis redisClient = RedisConnectionFactory.getConnection()) {
			redisClient.set(key, ObjectMapperUtils.getMapper().writeValueAsString(value));
			redisClient.expire(key, ttl);

			statisticsMonitor.updateTopFiveLFUKeys();
		} catch (JsonProcessingException e) {
			throw new CacheManagerException("Erro de operação: Set");
		}
	}

	public Object get(String key, Class<?> returnType) {
		try (Jedis redisClient = RedisConnectionFactory.getInstance().getConnection()) {
			Object value = ObjectMapperUtils.getMapper().readValue(redisClient.get(key), returnType);

			statisticsMonitor.updateTopFiveLFUKeys();

			return value;
		} catch (JsonProcessingException e) {
			throw new CacheManagerException("Erro de operação: Get");
		}
	}

	public void del(String key) {
		try (Jedis redisClient = RedisConnectionFactory.getInstance().getConnection()) {
			redisClient.del(key);
		}

		statisticsMonitor.updateTopFiveLFUKeys();
	}

	public boolean existsKey(String key) {
		try (Jedis redisClient = RedisConnectionFactory.getInstance().getConnection()) {
			return redisClient.exists(key);
		}
	}

}
