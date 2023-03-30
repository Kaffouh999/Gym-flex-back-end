package com.example.GymInTheBack.repositories;

import com.example.GymInTheBack.entities.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
