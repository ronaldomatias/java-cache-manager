package br.com.ronaldomatias.cachemanager.cachemanager.redis;

import br.com.ronaldomatias.cachemanager.config.JedisSingleton;
import br.com.ronaldomatias.cachemanager.util.ObjectMapperUtil;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;

@Log4j2
public class RedisOperations implements RedisOperationsInterface {
	// TODO: Remover sneaky
	private final Jedis client;

	public RedisOperations() {
		this.client = JedisSingleton.getInstance();
	}

	@SneakyThrows
	@Override
	public void set(String key, Object value, Long ttl) {
		client.set(key, ObjectMapperUtil.getMapper().writeValueAsString(value));
		client.expire(key, ttl);
		client.close();

		log.info("Set cache: " + value);
	}

	@SneakyThrows
	@Override
	public Object get(String key, Class<?> returnType) {
		Object value = ObjectMapperUtil.getMapper().readValue(client.get(key), returnType);
		client.close();

		log.info("Get Cache: " + value);
		return value;
	}

	@Override
	public void del(String key) {
		client.del(key);
		client.close();

		log.info("Del Cache: " + key);
	}

	@Override
	public boolean existsKey(String key) {
		boolean exists = client.exists(key);
		client.close();

		return exists;
	}

}