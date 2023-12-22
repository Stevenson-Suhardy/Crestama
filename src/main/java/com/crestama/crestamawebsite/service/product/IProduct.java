package com.crestama.crestamawebsite.service.product;

import com.crestama.crestamawebsite.entity.Product;

import java.util.List;

public interface IProduct {
    List<Product> findAll();

    Product findById(Long id);

    Product save(Product product);

    void deleteById(Long id);
}
