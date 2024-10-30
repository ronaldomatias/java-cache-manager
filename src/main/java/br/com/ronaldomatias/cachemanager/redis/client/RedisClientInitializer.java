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
		poolConfig.setMaxTotal(2); // Maximo total de conexoes.
		poolConfig.setMaxIdle(2);   // Maximo de conexoes ociosas.
		poolConfig.setMinIdle(1);   // Minimo de conexoes ociosas.

		jedisPool = new JedisPool(poolConfig, host, port);

		// Inicia a thread de subscribe
		startSubscriberExpiredKeys();
	}

	private static void startSubscriberExpiredKeys() {
		// Inicia um subscriber para ouvir o evento de expiração automatica das chaves.
		// Quando uma chave eh expirada, entao eh obrigatorio remove-la do Set 'lastRecentUsedKeys'.
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
				}, "__keyevent@0__:expired");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static Jedis getClient() {
		return jedisPool.getResource();
	}

}
