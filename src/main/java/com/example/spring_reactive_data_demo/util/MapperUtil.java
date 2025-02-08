package com.example.spring_reactive_data_demo.util;


import com.example.spring_reactive_data_demo.dto.ProductDto;
import com.example.spring_reactive_data_demo.entity.Product;
import org.springframework.beans.BeanUtils;

public class MapperUtil {

    public static Product DtoToEntity(ProductDto dto) {
        Product entity = new Product();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static ProductDto EntityToDto(Product entity) {
        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

}
