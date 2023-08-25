package com.crestama.crestamawebsite.service.product;

import com.crestama.crestamawebsite.dao.ProductRepository;
import com.crestama.crestamawebsite.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Overridden methods
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> result = productRepository.findById(id);

        Product product = null;

        if (result.isPresent()) {
            product = result.get();
        }
        else {
            throw new RuntimeException("Did not find product id - " + id);
        }

        return product;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
