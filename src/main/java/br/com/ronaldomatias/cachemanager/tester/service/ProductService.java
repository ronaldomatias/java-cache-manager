package br.com.ronaldomatias.cachemanager.tester.service;

import br.com.ronaldomatias.cachemanager.cachemanager.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

	@Cacheable(key = "categories", ttl = 60)
	public List<String> getCategories() {
		return Arrays.asList("Controlados", "Alimentos", "Genericos");
	}

}
