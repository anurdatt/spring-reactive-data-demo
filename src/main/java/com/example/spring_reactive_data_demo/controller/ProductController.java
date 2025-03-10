package com.example.spring_reactive_data_demo.controller;

import com.example.spring_reactive_data_demo.dto.ProductDto;
import com.example.spring_reactive_data_demo.service.ProductService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public Flux<ProductDto> getProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProduct(@PathVariable String id) {
        return service.getProduct(id);
    }

    @GetMapping("/range")
    public Flux<ProductDto> getProductsByPriceBetween(@RequestParam("minprice") double minprice, //String minprice,
                                                      @RequestParam("maxprice") double maxprice) {//String maxprice) {
        return service.getProductsByPriceBetween(minprice, maxprice); //Double.parseDouble(minprice), Double.parseDouble(maxprice));
    }

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono) {
        System.out.println("saveProduct controller method called!");

//        productDtoMono = productDtoMono.doOnNext(System.out::println);
        Mono<ProductDto> saved = service.saveProduct(productDtoMono.log());
        return saved;//.log();//.doOnNext(System.out::println);
    }

    @PutMapping("/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDtoMono, @PathVariable String id) {
        return service.updateProduct(productDtoMono, id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return service.deleteProduct(id);
    }
}
