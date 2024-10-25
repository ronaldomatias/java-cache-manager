package br.com.ronaldomatias.cachemanager.redis.manager;

import br.com.ronaldomatias.cachemanager.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public class LruAlgorithm {

	public void executeCleaningStrategy(Object value, Jedis redisClient) {
		// Pega a key com menos frequencia de uso para ser descartada.
		List<String> lastRecentUsedKeys = redisClient.zrange("lastRecentUsedKeys", 0, 1);
		if (CollectionUtils.isEmpty(lastRecentUsedKeys)) {
			// TODO: Exception dizendo que nao tem espaco para liberar OU libera qualquer uma.
		}

		String key = lastRecentUsedKeys.get(0);

		// Remove a key do cache e do 'lastRecentUsedKeys'.
		redisClient.del(key);
		redisClient.zrem("lastRecentUsedKeys", key);

		// Se ainda nao tem espaco disponivel, continua limpando.
		if (RedisCacheManager.valueReachesMemoryLimit(value, redisClient)) {
			this.executeCleaningStrategy(value, redisClient);
		}
	}

}
