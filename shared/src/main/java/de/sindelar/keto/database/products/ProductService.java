package de.sindelar.keto.database.products;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository repo;

	public Product create(Product product) {
		return repo.saveAndFlush(product);
	}

	public Product getById(String id) {
		return repo.findById(id).orElse(null);
	}

}
