package br.com.ronaldomatias.cachemanager.redis.client;

import br.com.ronaldomatias.cachemanager.util.ApplicationPropertiesUtil;
import redis.clients.jedis.Jedis;

public class RedisClientSingleton {
	private static volatile Jedis jedis;

	private RedisClientSingleton() {
	}

	public static Jedis getInstance() {
		if (jedis == null) {
			synchronized (RedisClientSingleton.class) {
				if (jedis == null) {
					final String host = ApplicationPropertiesUtil.getPropertyValue("cache-manager.redis.host");
					final int port = Integer.parseInt(ApplicationPropertiesUtil.getPropertyValue("cache-manager.redis.port"));

					jedis = new Jedis(host, port);
				}
			}
		}

		return jedis;
	}

}
