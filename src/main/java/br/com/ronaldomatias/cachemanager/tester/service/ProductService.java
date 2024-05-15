package br.com.ronaldomatias.cachemanager.tester.service;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import br.com.ronaldomatias.cachemanager.annotation.InvalidateCache;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

	@Cacheable(key = "categories", ttl = 60)
	public List<String> getCategories() {
		return Arrays.asList("Controlados", "Alimentos", "Genericos");
	}

	@InvalidateCache(key = "categories")
	public List<String> getMakers() {
		return Arrays.asList("Pharlab", "Cimed");
	}

}
