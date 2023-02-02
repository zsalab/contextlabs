package contextlabs.service;

import contextlabs.model.Product;
import reactor.core.publisher.Flux;

public interface ProductLoader {
	Flux<Product> getProducts();
}
