package br.com.ronaldomatias.cachemanager.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
@Log4j2
public class RedisBase implements RedisInterface {
	@Autowired
	Jedis client;

	@Override
	@SneakyThrows
	public void set(RedisDTO dto) {
		client.set(dto.getKey(), new ObjectMapper().writeValueAsString(dto.getValue())); // TODO: Criar um objmapperutil

		client.expire(dto.getKey(), dto.getTtl());
		log.info("Cacheado: " + dto.getValue());
	}

	@Override
	public String get(String key) {
		return client.get(key);
	}

	@Override
	public boolean existsKey(String key) {
		return client.exists(key);
	}


}