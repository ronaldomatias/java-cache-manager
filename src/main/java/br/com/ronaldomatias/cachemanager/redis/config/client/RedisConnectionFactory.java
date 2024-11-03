package br.com.ronaldomatias.cachemanager.redis.config.client;

import br.com.ronaldomatias.cachemanager.exception.CacheManagerException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnectionFactory {

	private static RedisConnectionFactory instance;

	private final JedisPool jedisPool;

	private RedisConnectionFactory(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public static synchronized void configure(String host, int port) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(2); // Maximo total de conexoes.
		poolConfig.setMaxIdle(2);  // Maximo de conexoes ociosas.
		poolConfig.setMinIdle(0);  // Minimo de conexoes ociosas.

		instance = new RedisConnectionFactory(new JedisPool(poolConfig, host, port));
	}

	public static RedisConnectionFactory getInstance() {
		if (instance == null || instance.jedisPool == null) {
			throw new CacheManagerException("Nenhuma conexão estabelecida. Configure a Classe RedisConnectionFactory para prosseguir.");
		}

		return instance;
	}

	public Jedis getConnection() {
		return jedisPool.getResource();
	}

}
