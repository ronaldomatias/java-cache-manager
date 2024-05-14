package br.com.ronaldomatias.cachemanager.tester.product;

import br.com.ronaldomatias.cachemanager.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

	@Cacheable(key = "categories", ttl = 10)
	public List<String> getCategories() {
		return Arrays.asList("Controlados", "Alimentos", "Genericos");
	}

}
