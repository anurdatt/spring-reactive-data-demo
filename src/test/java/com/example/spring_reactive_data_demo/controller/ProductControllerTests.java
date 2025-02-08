package com.example.spring_reactive_data_demo.controller;


import com.example.spring_reactive_data_demo.dto.ProductDto;
import com.example.spring_reactive_data_demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductService service;

    @Test
    public void getProductsTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("121", "TV", 1, 50000),
                new ProductDto("122", "Mobile", 1, 12000));

        when(service.getProducts()).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/products")
                .exchange()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("121", "TV", 1, 50000))
                .expectNext(new ProductDto("122", "Mobile", 1, 12000))
                .verifyComplete();
    }

    @Test
    public void getProductTest() {
        ProductDto productDto = new ProductDto("121", "watch", 1, 1000);
        Mono<ProductDto> productDtoMono = Mono.just(productDto);

        when(service.getProduct(any())).thenReturn(productDtoMono);
        webTestClient.get()
                .uri("/products/121")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ProductDto.class)
                .isEqualTo(productDto);
    }

    @Test
    public void getProductsByPriceBetweenTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("121", "TV", 1, 50000),
                new ProductDto("122", "Mobile", 1, 12000));

        when(service.getProductsByPriceBetween(10000, 70000)).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get()
                .uri("/products/range?minprice=10000&maxprice=70000")
                .exchange()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
//                .expectNextMatches(p -> p.getPrice() > 10000)
//                .expectNextMatches(p -> p.getPrice() < 70000)
                .expectNext(new ProductDto("121", "TV", 1, 50000))
                .expectNext(new ProductDto("122", "Mobile", 1, 12000))
                .verifyComplete();
    }

    @Test
    public void saveProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("121", "watch", 1, 1000));
        when(service.saveProduct(productDtoMono)).thenReturn(productDtoMono);

        webTestClient.post()
                .uri("/products")
                .body(productDtoMono, ProductDto.class)
                .exchange()
                .expectStatus().isOk(); //200
    }

    @Test
    public void updateProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("121", "watch", 1, 1000));

        when(service.updateProduct(productDtoMono, "343")).thenReturn(productDtoMono);

        webTestClient.put()
                .uri("/products/343")
                .body(productDtoMono, ProductDto.class)
                .exchange()
                .expectStatus().isOk(); //200
    }

    @Test
    public void deleteProductTest() {

        given(service.deleteProduct("343")).willReturn(Mono.empty());

        webTestClient.delete()
                .uri("/products/343")
                .exchange()
                .expectStatus().isOk(); //200
    }



}
