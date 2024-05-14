package br.com.ronaldomatias.cachemanager.cachemanager.redis;

import br.com.ronaldomatias.cachemanager.config.JedisSingleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;

@Log4j2
public class RedisOperations implements RedisOperationsInterface {
	private final Jedis client;

	public RedisOperations() {
		this.client = JedisSingleton.getInstance();
	}

	@SneakyThrows // TODO: Remover sneaky
	@Override
	public void set(String key, Object value, Long ttl) {
		client.set(key, new ObjectMapper().writeValueAsString(value)); // TODO: Criar um objmapperutil
		client.expire(key, ttl);

		log.info("Set cache: " + value);
	}

	@Override
	public String get(String key) {
		String get = client.get(key);

		log.info("Get Cache: " + get);
		return get;
	}

	@Override
	public boolean existsKey(String key) {
		return client.exists(key);
	}


}