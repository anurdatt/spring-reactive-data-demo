package com.example.spring_reactive_data_demo.repository;

import com.example.spring_reactive_data_demo.dto.ProductDto;
import com.example.spring_reactive_data_demo.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    Flux<ProductDto> findByPriceBetween(Range<Double> priceRange);
}
