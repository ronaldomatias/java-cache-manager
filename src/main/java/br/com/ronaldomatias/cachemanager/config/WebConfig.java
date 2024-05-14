package br.com.ronaldomatias.cachemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class WebConfig {

	@Bean
	public Jedis getJedis() {
		return new Jedis("localhost", 6379);
	}
}
