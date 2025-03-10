package com.example.spring_reactive_data_demo.service;

import com.example.spring_reactive_data_demo.dto.ProductDto;
import com.example.spring_reactive_data_demo.error.ProductNotFoundException;
import com.example.spring_reactive_data_demo.repository.ProductRepository;
import com.example.spring_reactive_data_demo.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Flux<ProductDto> getProducts() {
        return repository.findAll()
                .map(MapperUtil::EntityToDto);
    }

    public Mono<ProductDto> getProduct(String id) {
        return repository.findById(id)
                .map(MapperUtil::EntityToDto)
                .switchIfEmpty(Mono.error(new ProductNotFoundException()));
    }

    public Flux<ProductDto> getProductsByPriceBetween(double min, double max) {
        return repository.findByPriceBetween(Range.closed(min, max));
    }

    public Mono<ProductDto> saveProduct(ProductDto productDto) {
        return repository.insert(MapperUtil.DtoToEntity(productDto))
                .map(MapperUtil::EntityToDto);
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {

        return productDtoMono
//                .doOnNext(p -> System.out.println(p.getName()))
                .map(MapperUtil::DtoToEntity)
                .flatMap(repository::insert)
                .map(MapperUtil::EntityToDto);
    }

    public Mono<ProductDto> updateProduct(ProductDto productDto, String id) {
        return repository.findById(id)
                .map(e -> productDto)
//                .switchIfEmpty(Mono.error(new RuntimeException("FindById returned no item!!")))
                .doOnNext(dto -> dto.setId(id))
                .map(MapperUtil::DtoToEntity)
                .flatMap(repository::save)
                .map(MapperUtil::EntityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(p -> productDtoMono.map(MapperUtil::DtoToEntity))
                .doOnNext(e -> e.setId(id))
                .flatMap(repository::save)
                .map(MapperUtil::EntityToDto);
    }

    public Mono<Void> deleteProduct(String id) {
        return repository.deleteById(id);
    }

    public Mono<Void> deleteProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono.map(MapperUtil::DtoToEntity)
                .flatMap(repository::delete);
    }

}
