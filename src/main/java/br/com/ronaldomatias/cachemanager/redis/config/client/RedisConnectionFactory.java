package br.com.ronaldomatias.cachemanager.redis.config.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnectionFactory {

	private final JedisPool jedisPool;

	public RedisConnectionFactory(String host, int port) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(2);
		poolConfig.setMaxIdle(2);
		poolConfig.setMinIdle(0);

		this.jedisPool = new JedisPool(poolConfig, host, port);
	}

	public Jedis getConnection() {
		return jedisPool.getResource();
	}

}
