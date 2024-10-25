package br.com.ronaldomatias.cachemanager.redis.manager;

import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisCacheManager {
	// TODO: Avaliar criar um singleton dessa classe;
	private final LruAlgorithm lruAlgorithm;

	private final Jedis redisClient;

	public RedisCacheManager(Jedis redisClient) {
		this.lruAlgorithm  = new LruAlgorithm();
		this.redisClient = redisClient;
	}

	public void executeCleaningStrategy(Object value) {
		if (!valueReachesMemoryLimit(value, redisClient)) return;

		lruAlgorithm.executeCleaningStrategy(value, this.redisClient);
	}

	protected static boolean valueReachesMemoryLimit(Object value, Jedis redisClient) {
		Map<String, Object> memoryStats = redisClient.memoryStats();
		Long maxMemory = (Long) memoryStats.get("maxmemory");
		Long remainingBytesToOverhead = (Long) memoryStats.get("overhead.total");

		// SafetyBuffer de 1% do total da memoria disponivel.
		long safetyBuffer = maxMemory != null ? (long) Math.ceil(maxMemory * 0.01) : 1000;
		remainingBytesToOverhead -= safetyBuffer;

		return value.toString().getBytes().length > remainingBytesToOverhead;
	}

}
