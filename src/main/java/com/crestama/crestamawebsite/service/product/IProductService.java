package com.crestama.crestamawebsite.service.product;

import com.crestama.crestamawebsite.entity.Product;

import java.util.List;

public interface IProductService {
    List<Product> findAll();

    Product findById(int id);

    Product save(Product product);

    void deleteById(int id);
}
