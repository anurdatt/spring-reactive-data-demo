package com.example.spring_reactive_data_demo.service;


import com.example.spring_reactive_data_demo.dto.ProductDto;
import com.example.spring_reactive_data_demo.entity.Product;
import com.example.spring_reactive_data_demo.error.ProductNotFoundException;
import com.example.spring_reactive_data_demo.repository.ProductRepository;
import com.example.spring_reactive_data_demo.util.MapperUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private String TEST_PRODUCT_ID = "105";

    @Test
    public void saveProductTest() {
        ProductDto productDto = new ProductDto("101", "Laptop", 1, 40000);
        Mono<ProductDto> productDtoMono = Mono.just(productDto);

        when(repository.insert(MapperUtil.DtoToEntity(productDto))).thenReturn(Mono.just(MapperUtil.DtoToEntity(productDto)));

        Mono<ProductDto> response = service.saveProduct(productDtoMono);

        StepVerifier.create(response)
                .expectSubscription()
                .expectNext(productDto)
                .verifyComplete();
    }

    @Test
    public void getProductsTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("101", "Laptop", 1, 40000),
                new ProductDto("102", "TV", 1, 60000));

        when((repository.findAll())).thenReturn(productDtoFlux.map(MapperUtil::DtoToEntity));

        Flux<ProductDto> response = service.getProducts();

        StepVerifier.create(response)
                .expectSubscription()
                .expectNext(new ProductDto("101", "Laptop", 1, 40000))
                .expectNext(new ProductDto("102", "TV", 1, 60000))
                .verifyComplete();
    }

    @Test
    public void getProductTest() {
        Product product = Product.builder()
                .id(TEST_PRODUCT_ID)
                .name("Mobile")
                .quantity(1)
                .price(15000)
                .build();

        when(repository.findById(TEST_PRODUCT_ID)).thenReturn(Mono.just(product));

        Mono<ProductDto> productDtoMono = service.getProduct(TEST_PRODUCT_ID);
        StepVerifier.create(productDtoMono)
                .consumeNextWith(p -> {
                    assertEquals(p.getId(), TEST_PRODUCT_ID);
                })
                .verifyComplete();
    }

    @Test
    public void getProductTest_E_ProductNotFound() {
        when(repository.findById(TEST_PRODUCT_ID)).thenReturn(Mono.empty());

        Mono<ProductDto> productDtoMono = service.getProduct(TEST_PRODUCT_ID);
        StepVerifier.create(productDtoMono)
                .expectErrorMatches(e -> e instanceof ProductNotFoundException
                        && e.getMessage().equals("Product not found!"))
                .verify();
    }
}
