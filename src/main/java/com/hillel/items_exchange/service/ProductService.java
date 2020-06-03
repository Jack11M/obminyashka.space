package com.hillel.items_exchange.service;

import com.hillel.items_exchange.dao.ProductRepository;
import com.hillel.items_exchange.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}
