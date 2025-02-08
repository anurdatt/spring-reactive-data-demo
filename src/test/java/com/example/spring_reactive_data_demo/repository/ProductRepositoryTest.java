package com.example.spring_reactive_data_demo.repository;

import com.example.spring_reactive_data_demo.dto.ProductDto;
import com.example.spring_reactive_data_demo.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Range;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    public void shouldSaveSingleProduct() {
        Product product = Product.builder()
                .id("1")
                .name("Laptop")
                .price(45500.89)
                .quantity(1)
                .build();

        Publisher<Product> setup = repository.deleteAll().thenMany(repository.save(product));

        StepVerifier.create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void shouldSaveTwoProduct() {
        Product product = Product.builder()
                .id("1")
                .name("Laptop")
                .price(45500.89)
                .quantity(1)
                .build();

        Product product1 = Product.builder()
                .id("2")
                .name("Mobile")
                .price(16000)
                .quantity(1)
                .build();

        Flux<Product> setup = repository.deleteAll().thenMany(Flux.just(product, product1).flatMap(repository::save));

        StepVerifier.create(setup)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void shouldSaveProduct() {
        String TEST_ID = "101";
        Product product = Product.builder()
                .id(TEST_ID)
                .name("Mobile")
                .price(16000)
                .quantity(1)
                .build();
        Publisher<Product> setup = repository.deleteAll().thenMany(repository.save(product));
        Mono<Product> find = repository.findById(TEST_ID);

        Publisher<Product> composite = Mono.from(setup).then(find);

        StepVerifier.create(composite)
                .consumeNextWith(prod -> {
                    Assertions.assertNotNull(prod.getId());
                    Assertions.assertEquals(prod.getId(), TEST_ID);
                    Assertions.assertEquals(prod.getName(), "Mobile");
                    Assertions.assertEquals(prod.getPrice(), 16000);
                    Assertions.assertEquals(prod.getQuantity(), 1);
                })
                .verifyComplete();
    }

    @Test
    public void shouldFindByPriceBetween() {
        Product product = Product.builder()
                .id("102")
                .name("Mobile")
                .price(16000)
                .quantity(1)
                .build();
        Publisher<Product> setup = repository.deleteAll().then(repository.save(product));
        Flux<ProductDto> find = repository.findByPriceBetween(Range.closed(15000.0, 17000.0));

        Publisher<ProductDto> composite = Flux.from(setup).thenMany(find);

        StepVerifier.create(composite)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void shouldNotFindByPriceBetween() {
        Product product = Product.builder()
                .id("102")
                .name("Mobile")
                .price(18000)
                .quantity(1)
                .build();
        Publisher<Product> setup = repository.deleteAll().then(repository.save(product));
        Flux<ProductDto> find = repository.findByPriceBetween(Range.closed(15000.0, 17000.0));

        Publisher<ProductDto> composite = Flux.from(setup).thenMany(find);

        StepVerifier.create(composite)
                .expectNextCount(0)
                .verifyComplete();
    }
}
