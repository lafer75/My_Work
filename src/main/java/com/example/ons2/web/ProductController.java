package com.example.ons2.web;


import com.example.ons2.service.ProductService;
import com.example.ons2.until.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<Product> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProducts(page, size);
    }

    @GetMapping("/filter")
    public List<Product> filterProducts(@RequestParam(required = false) String country,
                                        @RequestParam(required = false) String type,
                                        @RequestParam(required = false) Double minPrice,
                                        @RequestParam(required = false) Double maxPrice) {
        return productService.filterProducts(country, type, minPrice != null ? minPrice : 0, maxPrice != null ? maxPrice : Double.MAX_VALUE);
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
