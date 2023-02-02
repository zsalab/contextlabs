package contextlabs.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import contextlabs.model.Product;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class CsvProductLoaderTest {
	@Autowired
	protected CsvProductLoader csvProductLoader;

	@Test
	void test() throws InterruptedException {
		Flux<Product> products = csvProductLoader.getProducts();
		StepVerifier.create(products)
			.expectNextCount(6)
			.verifyComplete();
	}

}
