package br.com.ronaldomatias.cachemanager.tester.controller;

import br.com.ronaldomatias.cachemanager.tester.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/categories")
	public List<String> getCategories() {
		return productService.getCategories();
	}

}
