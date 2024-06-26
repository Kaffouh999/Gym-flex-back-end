package com.binarybrothers.gymflexapi.repositories;

import com.binarybrothers.gymflexapi.entities.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
