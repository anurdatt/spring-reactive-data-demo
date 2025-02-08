package com.example.spring_reactive_data_demo.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document (collection = "Product")
public class Product {
    @Id
    String id;
    String name;
    int quantity;
    double price;

}
