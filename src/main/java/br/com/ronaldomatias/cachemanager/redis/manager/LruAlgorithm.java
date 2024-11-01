package br.com.ronaldomatias.cachemanager.redis.manager;

import br.com.ronaldomatias.cachemanager.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

public class LruAlgorithm {

	protected void executeCleaningStrategy(Object value, Jedis redisClient) {
		// Pega a key com menos frequencia de uso.
		List<String> lastRecentUsedKeys = redisClient.zrange("lastRecentUsedKeys", 0, 1);
		if (CollectionUtils.isEmpty(lastRecentUsedKeys)) {
			// TODO: Exception dizendo que nao tem espaco para liberar OU libera qualquer uma.
		}

		String key = lastRecentUsedKeys.get(0);

		// Remove a key do cache e do 'lastRecentUsedKeys'.
		redisClient.del(key);
		redisClient.zrem("lastRecentUsedKeys", key);

		// Se nao liberou espaco suficiente, refaz o processo.
		if (RedisCacheManager.valueReachesMemoryLimit(value, redisClient)) {
			this.executeCleaningStrategy(value, redisClient);
		}
	}

}
