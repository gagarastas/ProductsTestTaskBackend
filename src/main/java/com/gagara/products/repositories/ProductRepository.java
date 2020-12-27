package com.gagara.products.repositories;

import com.gagara.products.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product as p where p.id = :id ")
    Product getOne(Long id);

    @Query("select p from Product as p where p.name = :name")
    List<Product> getOneByName(String name);
}
