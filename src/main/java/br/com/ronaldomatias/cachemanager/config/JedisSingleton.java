package br.com.ronaldomatias.cachemanager.config;

import redis.clients.jedis.Jedis;

public class JedisSingleton {
	private static Jedis jedis;

	public static Jedis getInstance() {
		if (jedis == null) {
			jedis = new Jedis("localhost", 6379);
		}

		return jedis;
	}

}
