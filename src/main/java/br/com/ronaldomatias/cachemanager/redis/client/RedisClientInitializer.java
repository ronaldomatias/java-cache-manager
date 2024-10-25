package br.com.ronaldomatias.cachemanager.redis.client;

import br.com.ronaldomatias.cachemanager.util.ApplicationPropertiesUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class RedisClientInitializer {
	private static final JedisPool jedisPool;

	static {
		final String host = ApplicationPropertiesUtils.getPropertyValue("cache-manager.redis.host");
		final int port = Integer.parseInt(ApplicationPropertiesUtils.getPropertyValue("cache-manager.redis.port"));

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(10); // Maximo total de conexoes.
		poolConfig.setMaxIdle(5);   // Maximo de conexoes ociosas.
		poolConfig.setMinIdle(2);   // Minimo de conexoes ociosas.

		jedisPool = new JedisPool(poolConfig, host, port);

		// Inicia a thread de subscribe
		startExpiredKeysSubscriber();
	}

	private static void startExpiredKeysSubscriber() {
		new Thread(() -> {
			try (Jedis jedisInstance = jedisPool.getResource()) {
				jedisInstance.psubscribe(new JedisPubSub() {
					@Override
					public void onPMessage(String pattern, String channel, String message) {
						try (Jedis jedis = jedisPool.getResource()) {
							jedis.zrem("lastRecentUsedKeys", message);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, "__keyevent@0__:expired"); // Assinando o evento de expiração
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static Jedis getClient() {
		return jedisPool.getResource();
	}
}
