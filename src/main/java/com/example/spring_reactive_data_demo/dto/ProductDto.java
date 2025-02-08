package com.example.spring_reactive_data_demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    String id;
    String name;
    int quantity;
    double price;

}
