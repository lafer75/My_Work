package com.example.ons2.repository;


import com.example.ons2.until.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}