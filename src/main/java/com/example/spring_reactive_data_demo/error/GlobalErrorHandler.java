package com.example.spring_reactive_data_demo.error;


import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.net.URI;


@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(value = ProductNotFoundException.class)
    public Mono<ErrorResponse> handleError(ProductNotFoundException pnfe) {
        return Mono.just(ErrorResponse.builder(pnfe, HttpStatusCode.valueOf(404), pnfe.getDetails())
                .type(URI.create(pnfe.getClass().getName()))
                .title(pnfe.getMessage())
                .build());
    }
    @ExceptionHandler
    public Mono<ErrorResponse> handleError(Exception e) {
            return Mono.just(ErrorResponse.builder(e, HttpStatusCode.valueOf(500), "Unknown Server error!")
                    .build());

    }
}
