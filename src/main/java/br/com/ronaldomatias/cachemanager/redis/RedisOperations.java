package br.com.ronaldomatias.cachemanager.redis;

import br.com.ronaldomatias.cachemanager.cachestrategy.SmartAlgorithm;
import br.com.ronaldomatias.cachemanager.redis.client.RedisClientSingleton;
import br.com.ronaldomatias.cachemanager.util.ObjectMapperUtil;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisOperations {
	// TODO: Remover sneaky e retornar feedback na exception.
	private final Jedis client;

	private final SmartAlgorithm smartAlgorithm;

	public RedisOperations() {
		this.client = RedisClientSingleton.getInstance();
		this.smartAlgorithm = new SmartAlgorithm();
	}

	@SneakyThrows
	public void set(String key, Object value, Long ttl) {
		if (this.valueReachesMemoryLimit(value)) {
			smartAlgorithm.executeCleaningStrategy(value);
		}

		client.set(key, ObjectMapperUtil.getMapper().writeValueAsString(value));
		client.expire(key, ttl);
		client.close();
	}

	@SneakyThrows
	public Object get(String key, Class<?> returnType) {
		Object value = ObjectMapperUtil.getMapper().readValue(client.get(key), returnType);
		client.close();

		smartAlgorithm.getKeyStats().addRecentUsedLast(key);

		return value;
	}

	public void del(String key) {
		client.del(key);
		client.close();

		smartAlgorithm.getKeyStats().getRecentUsedLast().remove(key);
	}

	public boolean existsKey(String key) {
		boolean exists = client.exists(key);
		client.close();

		return exists;
	}

	private boolean valueReachesMemoryLimit(Object value) {
		Map<String, Object> memoryStats = client.memoryStats();
		Long remainingBytesToOverhead = (Long) memoryStats.get("overhead.total");
		Long safetyBuffer = 1000L;

		remainingBytesToOverhead -= safetyBuffer;
		return value.toString().getBytes().length > remainingBytesToOverhead;
	}

}