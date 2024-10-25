package br.com.ronaldomatias.cachemanager.redis.client;

import br.com.ronaldomatias.cachemanager.util.ApplicationPropertiesUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisClientSingleton {
	private static volatile Jedis jedis;

	private RedisClientSingleton() {
	}

	public static Jedis getInstance() {
		if (jedis == null) {
			synchronized (RedisClientSingleton.class) {
				if (jedis == null) {
					final String host = ApplicationPropertiesUtils.getPropertyValue("cache-manager.redis.host");
					final int port = Integer.parseInt(ApplicationPropertiesUtils.getPropertyValue("cache-manager.redis.port"));

					jedis = new Jedis(host, port);
					jedis.configSet("notify-keyspace-events", "Ex");
					jedis.psubscribe(getPsubscribe());
				}
			}
		}

		return jedis;
	}

	public static JedisPubSub getPsubscribe() {
		// Cria um listenner para que seja possivel remover do set 'lastRecentUsedKeys' as keys expiradas automaticamente pelo Redis.
		return new JedisPubSub() {
			@Override
			public void onPMessage(String pattern, String channel, String message) {
				// O 'message' é a key que expirou.
				jedis.zrem("lastRecentUsedKeys", message);
			}
		};
	}

}
